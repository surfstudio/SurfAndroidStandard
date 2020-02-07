package ru.surfstudio.navigation.navigator

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import ru.surfstudio.navigation.animation.BaseScreenAnimations
import ru.surfstudio.navigation.animation.NoScreenAnimations
import ru.surfstudio.navigation.command.Add
import ru.surfstudio.navigation.command.NavigationCommand
import ru.surfstudio.navigation.command.Replace
import ru.surfstudio.navigation.extension.fragment.setAnimations
import ru.surfstudio.navigation.navigator.backstack.fragment.FragmentBackStack
import ru.surfstudio.navigation.navigator.backstack.fragment.entry.FragmentBackStackEntry
import ru.surfstudio.navigation.navigator.backstack.fragment.entry.FragmentBackStackEntryObj
import ru.surfstudio.navigation.navigator.backstack.fragment.listener.BackStackChangedListener
import ru.surfstudio.navigation.navigator.backstack.route.BackStackRoute
import ru.surfstudio.navigation.navigator.fragment.FragmentNavigatorInterface
import ru.surfstudio.navigation.route.fragment.FragmentRoute

/**
 * Fragment navigator.
 *
 * Has it's own implementation for backStack. To save it on configuration changes you must call
 * [onSaveState] and [onRestoreState] methods in specific lifecycle callbacks.
 *
 * Позволяет задавать кастомные анимации переключения фрагментов.
 */
open class FragmentNavigator(
        override val fragmentManager: FragmentManager,
        override var containerId: Int = -1
) : FragmentNavigatorInterface {

    protected val backStack: FragmentBackStack = FragmentBackStack()

    override val backStackEntryCount: Int
        get() = backStack.size

    private val backStackChangedListeners = arrayListOf<BackStackChangedListener>()

    override fun add(route: FragmentRoute, animations: BaseScreenAnimations) {
        val fragmentManager = fragmentManager
        fragmentManager.executePendingTransactions()

        val backStackTag = convertToBackStackTag(route.getTag())
        val fragment = route.createFragment()

        fragmentManager.beginTransaction().apply {
            setAnimations(animations)
            add(containerId, fragment, backStackTag)
            addToBackStack(
                    FragmentBackStackEntry(
                            backStackTag,
                            fragment,
                            Add(BackStackRoute(route.getTag()), animations)
                    )
            )
            commit()
        }
    }


    override fun replace(route: FragmentRoute, animations: BaseScreenAnimations) {
        val fragmentManager = fragmentManager
        fragmentManager.executePendingTransactions()

        val backStackTag = convertToBackStackTag(route.getTag())
        val fragment = route.createFragment()
        val lastFragment = backStack.peekFragment()

        fragmentManager.beginTransaction().apply {
            setAnimations(animations)
            detachIfNotNull(lastFragment)
            add(containerId, fragment, backStackTag)
            addToBackStack(
                    FragmentBackStackEntry(
                            backStackTag,
                            fragment,
                            Replace(BackStackRoute(route.getTag()), animations)
                    )
            )
            commit()
        }
    }

    /**
     * @return возвращает `true` если фрагмент был удален успешно
     */
    override fun remove(
            route: FragmentRoute,
            animations: BaseScreenAnimations
    ): Boolean {
        val fragmentManager = fragmentManager
        fragmentManager.executePendingTransactions()

        val fragment = findFragment(convertToBackStackTag(route.getTag())) ?: return false

        fragmentManager.beginTransaction()
                .setAnimations(animations)
                .remove(fragment)
                .commit()
        return true
    }

    private fun findFragment(backStackTag: String): Fragment? =
            backStack.findFragment(backStackTag)

    /**
     * @return возвращает `true` если фрагмент успешно отобразился
     */
    override fun show(route: FragmentRoute, animations: BaseScreenAnimations): Boolean {
        return toggleVisibility(route, true, animations)
    }

    /**
     * @return возвращает `true` если фрагмент был скрыт успешно
     */
    override fun hide(route: FragmentRoute, animations: BaseScreenAnimations): Boolean {
        return toggleVisibility(route, false, animations)
    }

    override fun removeLast(animations: BaseScreenAnimations): Boolean {
        val fragmentManager = fragmentManager
        fragmentManager.executePendingTransactions()

        fragmentManager.beginTransaction().run {
            val entry = backStack.pop()
            setupReverseAnimations(this, entry.command, animations)
            when (entry.command) {
                is Add -> setupReverseAdd(this, entry)
                is Replace -> setupReverseReplace(this, entry)
            }
            commit()
        }
        return true
    }

    override fun replaceHard(route: FragmentRoute, animations: BaseScreenAnimations) {
        val fragmentManager = fragmentManager
        fragmentManager.executePendingTransactions()

        val backStackTag = convertToBackStackTag(route.getTag())
        val fragment = route.createFragment()

        fragmentManager.beginTransaction().apply {
            setAnimations(animations)
            remove(backStack.pop().fragment)
            add(containerId, fragment, backStackTag)
            addToBackStack(
                    FragmentBackStackEntry(
                            backStackTag,
                            fragment,
                            Replace(BackStackRoute(route.getTag()), animations)
                    )
            )
            commit()
        }
    }

    /**
     * Очищает стек фрагментов до роута.
     *
     * Пример:
     * Фрагменты А, Б, С, Д добавлены в стек
     * popBackStack(Б, true) очистит стек до Б включительно
     * то есть, останется в стеке только А
     * или
     * popBackStack(Б, false) в стеке останется А и Б,
     * Б не удаляется из стека
     *
     * @param route очистка до уровня роута
     * @param isInclusive удалить стек включая и роут
     *
     * @return возвращает `true` если фрагмент(ы) был(и) удален(ы) из стека
     */
    override fun removeUntil(route: FragmentRoute, isInclusive: Boolean): Boolean {

        val backStackTag = convertToBackStackTag(route.getTag())
        val entry = backStack.find(backStackTag) ?: return false

        fragmentManager.executePendingTransactions()
        fragmentManager.beginTransaction()
                .apply {

                    var lastBackStackEntry: FragmentBackStackEntry = backStack.pop()

                    while (lastBackStackEntry != entry) {
                        remove(lastBackStackEntry.fragment)
                        lastBackStackEntry = backStack.pop()
                    }

                    if (isInclusive) {
                        remove(lastBackStackEntry.fragment)
                    }
                    // добавляем
                    backStack.peekFragment()?.let(::attach)
                }
                .commitNow()
        notifyBackStackListeners()
        return true
    }

    override fun removeAll(): Boolean {
        fragmentManager.executePendingTransactions()
        fragmentManager
                .beginTransaction()
                .apply {
                    val backStackSize = backStack.size
                    repeat(backStackSize - 1) {
                        val entry = backStack.pop()
                        remove(entry.fragment)
                    }
                    backStack.peekFragment()?.let(::attach)
                }
                .commitNow()
        notifyBackStackListeners()
        return true
    }

    override fun onSaveState(outState: Bundle?) {
        outState ?: return
        val outStack = backStack.map { FragmentBackStackEntryObj.from(it) }
        val stackKey = BACK_STACK_KEY.format(containerId)
        outState.putSerializable(stackKey, ArrayList(outStack))
    }

    override fun onRestoreState(savedInstanceState: Bundle?) {
        val stackKey = BACK_STACK_KEY.format(containerId)
        val inStack = savedInstanceState?.getSerializable(stackKey) as? ArrayList<FragmentBackStackEntryObj>
                ?: return
        inStack
                .mapNotNull { entryObj ->
                    val fragment = fragmentManager.findFragmentByTag(entryObj.tag)
                    fragment?.let { FragmentBackStackEntry(entryObj.tag, it, entryObj.command) }
                }
                .forEach { entry ->
                    backStack.push(entry)
                }
    }

    override fun addBackStackChangeListener(listener: BackStackChangedListener) {
        backStackChangedListeners.add(listener)
    }

    override fun removeBackStackChangeListener(listener: BackStackChangedListener) {
        backStackChangedListeners.remove(listener)
    }

    protected fun convertToBackStackTag(routeTag: String): String {
        return "$containerId-$routeTag"
    }

    protected fun convertToRouteTag(backStackTag: String): String {
        return backStackTag.split("-")[1]
    }

    private fun toggleVisibility(
            route: FragmentRoute,
            shouldShow: Boolean,
            animationBundle: BaseScreenAnimations
    ): Boolean {
        val fragmentManager = fragmentManager
        fragmentManager.executePendingTransactions()

        val fragment = fragmentManager.findFragmentByTag(convertToBackStackTag(route.getTag()))
                ?: return false

        fragmentManager.beginTransaction()
                .apply {
                    setAnimations(animationBundle)
                    if (shouldShow) show(fragment) else hide(fragment)
                    commit()
                }

        return true
    }

    /**
     * Добавление анимаций для выполнения обратной операции
     */
    private fun setupReverseAnimations(
            transaction: FragmentTransaction,
            command: NavigationCommand,
            overridingAnimations: BaseScreenAnimations
    ) {
        val lastAnimations = when (command) {
            is Add -> command.animations
            is Replace -> command.animations
            else -> NoScreenAnimations
        }

        if (lastAnimations != NoScreenAnimations || overridingAnimations != NoScreenAnimations) {
            val shouldOverrideAnimations = overridingAnimations != NoScreenAnimations
            val transactionAnimations = if (shouldOverrideAnimations) overridingAnimations else lastAnimations

            transaction.setAnimations(transactionAnimations, true)
        }
    }

    /**
     * Выполнение операции, обратной стандартному replace
     */
    private fun setupReverseReplace(transaction: FragmentTransaction, entry: FragmentBackStackEntry) {
        val lastFragment = backStack.peekFragment()
        transaction.remove(entry.fragment)
        transaction.attach(lastFragment ?: return)
        notifyBackStackListeners()
    }

    /**
     * Выполнение операции, обратной стандартному add
     */
    private fun setupReverseAdd(transaction: FragmentTransaction, entry: FragmentBackStackEntry) {
        transaction.remove(entry.fragment)
        notifyBackStackListeners()
    }

    private fun FragmentTransaction.detachIfNotNull(fragment: Fragment?) =
            apply { fragment?.let(::detach) }

    private fun addToBackStack(entry: FragmentBackStackEntry) {
        backStack.push(entry)
        notifyBackStackListeners()
    }

    private fun notifyBackStackListeners() = backStackChangedListeners.forEach { it.invoke(backStack) }

    companion object {
        private const val BACK_STACK_KEY = "TabChildFragmentNavigator container %d"
    }
}
