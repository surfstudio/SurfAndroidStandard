package ru.surfstudio.android.navigation.navigator.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import ru.surfstudio.android.navigation.animation.BaseScreenAnimations
import ru.surfstudio.android.navigation.animation.NoScreenAnimations
import ru.surfstudio.android.navigation.command.fragment.Add
import ru.surfstudio.android.navigation.command.NavigationCommand
import ru.surfstudio.android.navigation.command.fragment.Replace
import ru.surfstudio.android.navigation.extension.fragment.setAnimations
import ru.surfstudio.android.navigation.navigator.backstack.fragment.FragmentBackStack
import ru.surfstudio.android.navigation.navigator.backstack.fragment.entry.FragmentBackStackEntry
import ru.surfstudio.android.navigation.navigator.backstack.fragment.entry.FragmentBackStackEntryObj
import ru.surfstudio.android.navigation.navigator.backstack.fragment.listener.BackStackChangedListener
import ru.surfstudio.android.navigation.navigator.backstack.fragment.BackStackRoute
import ru.surfstudio.android.navigation.route.fragment.FragmentRoute

/**
 * Fragment navigator.
 *
 * Has it's own implementation for back stack: [FragmentBackStack].
 *
 * You can observe back stack changes by calling [addBackStackChangeListener].
 * You must not forget to remove these listener when there's no need in them
 * by calling [removeBackStackChangeListener] to avoid memory leak.
 *
 * To save back stack on configuration changes you must call
 * [onSaveState] and [onRestoreState] methods in corresponding lifecycle callbacks of parent screen.
 */
open class FragmentNavigator(
        override val fragmentManager: FragmentManager,
        override val containerId: Int = View.NO_ID
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
            lastFragment?.let(::detach) //detach fragment if not null
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

    override fun show(route: FragmentRoute, animations: BaseScreenAnimations): Boolean {
        return toggleVisibility(route, true, animations)
    }

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
        val stackKey = getBackStackKey()
        outState.putSerializable(stackKey, ArrayList(outStack))
    }

    override fun onRestoreState(savedInstanceState: Bundle?) {
        val stackKey = getBackStackKey()
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

    /**
     * Add listener to observe back stack changes
     */
    fun addBackStackChangeListener(listener: BackStackChangedListener) {
        backStackChangedListeners.add(listener)
    }

    /**
     * Remove listener used to observe back stack changes
     */
    fun removeBackStackChangeListener(listener: BackStackChangedListener) {
        backStackChangedListeners.remove(listener)
    }

    protected fun convertToBackStackTag(routeTag: String): String {
        return "$containerId-$routeTag"
    }

    protected fun convertToRouteTag(backStackTag: String): String {
        return backStackTag.split("-")[1]
    }

    protected open fun getBackStackKey() = BACK_STACK_KEY.format(containerId)

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
     * Add animations to a reverse operations.
     */
    private fun setupReverseAnimations(
            transaction: FragmentTransaction,
            command: NavigationCommand,
            overridingAnimations: BaseScreenAnimations
    ) {
        val lastAnimations = when (command) {
            is Add -> command.animations as BaseScreenAnimations
            is Replace -> command.animations as BaseScreenAnimations
            else -> NoScreenAnimations
        }

        if (lastAnimations != NoScreenAnimations || overridingAnimations != NoScreenAnimations) {
            val shouldOverrideAnimations = overridingAnimations != NoScreenAnimations
            val transactionAnimations = if (shouldOverrideAnimations) overridingAnimations else lastAnimations

            transaction.setAnimations(transactionAnimations, true)
        }
    }

    /**
     * Perform opertation opposite to ordinary [replace]
     */
    private fun setupReverseReplace(transaction: FragmentTransaction, entry: FragmentBackStackEntry) {
        val lastFragment = backStack.peekFragment()
        transaction.remove(entry.fragment)
        transaction.attach(lastFragment ?: return)
        notifyBackStackListeners()
    }

    /**
     * Perform operation opposite to ordinary [add].
     */
    private fun setupReverseAdd(transaction: FragmentTransaction, entry: FragmentBackStackEntry) {
        transaction.remove(entry.fragment)
        notifyBackStackListeners()
    }

    private fun addToBackStack(entry: FragmentBackStackEntry) {
        backStack.push(entry)
        notifyBackStackListeners()
    }

    private fun notifyBackStackListeners() = backStackChangedListeners.forEach { it.invoke(backStack) }

    companion object {
        private const val BACK_STACK_KEY = "TabChildFragmentNavigator container %d"
    }
}
