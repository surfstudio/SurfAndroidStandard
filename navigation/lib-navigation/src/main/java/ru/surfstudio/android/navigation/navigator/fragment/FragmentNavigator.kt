package ru.surfstudio.android.navigation.navigator.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import ru.surfstudio.android.navigation.animation.Animations
import ru.surfstudio.android.navigation.animation.resource.BaseResourceAnimations
import ru.surfstudio.android.navigation.animation.resource.NoResourceAnimations
import ru.surfstudio.android.navigation.animation.utils.FragmentAnimationSupplier
import ru.surfstudio.android.navigation.backstack.fragment.BackStackFragmentRoute
import ru.surfstudio.android.navigation.backstack.fragment.FragmentBackStack
import ru.surfstudio.android.navigation.backstack.fragment.entry.FragmentBackStackEntry
import ru.surfstudio.android.navigation.backstack.fragment.entry.FragmentBackStackEntryObj
import ru.surfstudio.android.navigation.backstack.fragment.listener.FragmentBackStackChangedListener
import ru.surfstudio.android.navigation.command.NavigationCommand
import ru.surfstudio.android.navigation.command.fragment.Add
import ru.surfstudio.android.navigation.command.fragment.Replace
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
        protected val fragmentManager: FragmentManager,
        protected val containerId: Int,
        savedState: Bundle? = null
) : FragmentNavigatorInterface {

    protected val backStack: FragmentBackStack = FragmentBackStack()

    override val backStackEntryCount: Int
        get() = backStack.size

    private val backStackChangedListeners = arrayListOf<FragmentBackStackChangedListener>()

    protected open var animationSupplier = FragmentAnimationSupplier()

    init {
        onRestoreState(savedState)
    }

    override fun add(route: FragmentRoute, animations: Animations) {
        val backStackTag = convertToBackStackTag(route.getId())
        val fragment = route.createFragment()

        fragmentManager.beginTransaction().apply {
            supplyWithAnimations(animations)
            add(containerId, fragment, backStackTag)
            addToBackStack(
                    FragmentBackStackEntry(
                            backStackTag,
                            fragment,
                            Add(BackStackFragmentRoute(route.getId()), animations)
                    )
            )
            commitNow()
        }
    }


    override fun replace(route: FragmentRoute, animations: Animations) {
        val backStackTag = convertToBackStackTag(route.getId())
        val fragment = route.createFragment()

        fragmentManager.beginTransaction().apply {
            supplyWithAnimations(animations)
            findLastVisibleFragments().forEach { detach(it) }
            add(containerId, fragment, backStackTag)
            addToBackStack(
                    FragmentBackStackEntry(
                            backStackTag,
                            fragment,
                            Replace(BackStackFragmentRoute(route.getId()), animations)
                    )
            )
            commitNow()
        }
    }

    override fun remove(route: FragmentRoute, animations: Animations) {
        val fragment = findFragment(convertToBackStackTag(route.getId())) ?: return

        fragmentManager.beginTransaction()
                .supplyWithAnimations(animations)
                .remove(fragment)
                .commit()
    }

    override fun removeLast(animations: Animations) {
        fragmentManager.beginTransaction().run {
            val entry = backStack.pop()
            setupReverseAnimations(this, entry.command, animations)
            when (entry.command) {
                is Add -> setupReverseAdd(this, entry)
                is Replace -> setupReverseReplace(this, entry)
            }
            commitNow()
        }
        notifyBackStackListeners()
    }

    override fun replaceHard(route: FragmentRoute, animations: Animations) {
        val backStackTag = convertToBackStackTag(route.getId())
        val fragment = route.createFragment()

        fragmentManager.beginTransaction().apply {
            supplyWithAnimations(animations)
            remove(backStack.pop().fragment)
            add(containerId, fragment, backStackTag)
            addToBackStack(
                    FragmentBackStackEntry(
                            backStackTag,
                            fragment,
                            Replace(BackStackFragmentRoute(route.getId()), animations)
                    )
            )
            commitNow()
        }
        notifyBackStackListeners()
    }

    override fun removeUntil(route: FragmentRoute, animations: Animations, isInclusive: Boolean) {
        val backStackTag = convertToBackStackTag(route.getId())
        val entry = backStack.find(backStackTag) ?: return
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
                    // добавляем видимые фрагменты
                    findLastVisibleEntries().forEach { entry ->
                        setupReverseAnimations(this, entry.command, animations)
                        attach(entry.fragment)
                    }
                }
                .commitNow()
        notifyBackStackListeners()
    }

    override fun removeAll(animations: Animations, shouldRemoveLast: Boolean) {
        fragmentManager
                .beginTransaction()
                .apply {
                    val backStackSize = backStack.size
                    val removalCount = if (shouldRemoveLast) backStackSize else backStackSize - 1
                    repeat(removalCount) {
                        val entry = backStack.pop()
                        remove(entry.fragment)
                    }
                    //Добавляем последний видимый фрагмент (если есть)
                    findLastVisibleEntries().forEach { entry ->
                        setupReverseAnimations(this, entry.command, animations)
                        attach(entry.fragment)
                    }
                }
                .commitNow()
        notifyBackStackListeners()
    }

    override fun show(route: FragmentRoute, animations: Animations) {
        toggleVisibility(route, true, animations)
    }

    override fun hide(route: FragmentRoute, animations: Animations) {
        toggleVisibility(route, false, animations)
    }

    override fun onSaveState(outState: Bundle?) {
        outState ?: return
        val outStack = backStack.map { FragmentBackStackEntryObj.from(it) }
        val stackKey = getBackStackKey()
        outState.putSerializable(stackKey, ArrayList(outStack))
    }

    final override fun onRestoreState(savedInstanceState: Bundle?) {
        val stackKey = getBackStackKey()
        val inStack = savedInstanceState?.getSerializable(stackKey) as? ArrayList<FragmentBackStackEntryObj>
                ?: return
        inStack
                .mapNotNull { entryObj ->
                    val fragment = fragmentManager.findFragmentByTag(entryObj.tag)
                    fragment?.let { entryObj.toEntry(fragment) }
                }
                .forEach { entry ->
                    backStack.push(entry)
                }
    }

    /**
     * Add listener to observe back stack changes
     */
    override fun addBackStackChangeListener(listener: FragmentBackStackChangedListener) {
        backStackChangedListeners.add(listener)
    }

    /**
     * Remove listener used to observe back stack changes
     */
    override fun removeBackStackChangeListener(listener: FragmentBackStackChangedListener) {
        backStackChangedListeners.remove(listener)
    }

    protected open fun convertToBackStackTag(routeTag: String): String {
        return "$containerId$ROUTE_TAG_DELIMITER$routeTag"
    }

    protected open fun extractRouteTag(backStackTag: String): String {
        return backStackTag.split(ROUTE_TAG_DELIMITER).last()
    }

    protected open fun getBackStackKey(): String {
        return BACK_STACK_KEY.format(containerId)
    }

    /**
     * Finds fragment by its [backStackTag]
     */
    protected open fun findFragment(backStackTag: String): Fragment? =
            backStack.findFragment(backStackTag)

    /**
     * Extracts fragments from backStack which needs to be attached to a view.
     *
     * We cant just extract last visible fragment because several fragments can be visible if we
     * add them by using [Add] navigation command.
     *
     * So, we're attaching all fragments until the command is not [Add].
     */
    open fun findLastVisibleFragments(): List<Fragment> =
            findLastVisibleEntries().map { it.fragment }

    /**
     * Extracts entries from backStack with fragments which needs to be attached to a view.
     *
     * We cant just extract last visible fragment because several fragments can be visible if we
     * add them by using [Add] navigation command.
     *
     * So, we're attaching all entries until the command is not [Add].
     */
    open fun findLastVisibleEntries(): List<FragmentBackStackEntry> {
        val entries = mutableListOf<FragmentBackStackEntry>()
        var index = backStack.lastIndex
        var entry: FragmentBackStackEntry? = backStack.getOrNull(index) ?: return emptyList()
        do {
            entries.add(entry!!)
            index--
            entry = backStack.getOrNull(index)
        } while (entry != null && entry.command is Add)
        return entries.reversed()
    }

    /**
     * Add animations to a reverse operations.
     */
    protected open fun setupReverseAnimations(
            transaction: FragmentTransaction,
            command: NavigationCommand,
            overridingAnimations: Animations
    ) {
        val lastAnimations = when (command) {
            is Add -> command.animations as BaseResourceAnimations
            is Replace -> command.animations as BaseResourceAnimations
            else -> NoResourceAnimations
        }

        if (lastAnimations != NoResourceAnimations || overridingAnimations != NoResourceAnimations) {
            val shouldOverrideAnimations = overridingAnimations != NoResourceAnimations
            val transactionAnimations = if (shouldOverrideAnimations && overridingAnimations is BaseResourceAnimations) {
                overridingAnimations
            } else {
                lastAnimations
            }
            animationSupplier.setResourceAnimations(transaction, transactionAnimations, true)
        }
    }

    /**
     * Perform operation opposite to ordinary [replace]
     */
    protected open fun setupReverseReplace(transaction: FragmentTransaction, entry: FragmentBackStackEntry) {
        transaction.remove(entry.fragment)
        findLastVisibleFragments().forEach { transaction.attach(it) }
    }

    /**
     * Perform operation opposite to ordinary [add].
     */
    protected open fun setupReverseAdd(transaction: FragmentTransaction, entry: FragmentBackStackEntry) {
        transaction.remove(entry.fragment)
    }

    protected fun FragmentTransaction.supplyWithAnimations(
            animations: Animations
    ): FragmentTransaction {
        return animationSupplier.supplyWithAnimations(this, animations)
    }

    protected fun toggleVisibility(
            route: FragmentRoute,
            shouldShow: Boolean,
            animationBundle: Animations
    ) {
        val tag = convertToBackStackTag(route.getId())
        val fragment = fragmentManager.findFragmentByTag(tag)
                ?: return

        fragmentManager.beginTransaction()
                .apply {
                    supplyWithAnimations(animationBundle)
                    if (shouldShow) show(fragment) else hide(fragment)
                    commit()
                }
    }

    protected fun addToBackStack(entry: FragmentBackStackEntry) {
        backStack.push(entry)
        notifyBackStackListeners()
    }

    protected fun notifyBackStackListeners() {
        val stackCopy = backStack.copy()
        backStackChangedListeners.forEach { it.invoke(stackCopy) }
    }

    companion object {
        protected const val BACK_STACK_KEY = "FragmentNavigator container %d"
        const val ROUTE_TAG_DELIMITER = "-"
    }
}
