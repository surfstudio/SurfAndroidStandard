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
        val lastFragment = backStack.peekFragment()

        fragmentManager.beginTransaction().apply {
            supplyWithAnimations(animations)
            lastFragment?.let(::detach) //detach fragment if not null
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

    private fun findFragment(backStackTag: String): Fragment? =
            backStack.findFragment(backStackTag)

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
    }

    override fun removeUntil(route: FragmentRoute, isInclusive: Boolean) {
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
                    // добавляем
                    backStack.peekFragment()?.let(::attach)
                }
                .commitNow()
        notifyBackStackListeners()
    }

    override fun removeAll(shouldRemoveLast: Boolean) {
        fragmentManager
                .beginTransaction()
                .apply {
                    val backStackSize = backStack.size
                    val removalCount = if (shouldRemoveLast) backStackSize else backStackSize - 1
                    repeat(removalCount) {
                        val entry = backStack.pop()
                        remove(entry.fragment)
                    }
                    backStack.peekFragment()?.let(::attach)
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

    private fun FragmentTransaction.supplyWithAnimations(
            animations: Animations
    ): FragmentTransaction {
        return animationSupplier.supplyWithAnimations(this, animations)
    }

    private fun toggleVisibility(
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

    private fun addToBackStack(entry: FragmentBackStackEntry) {
        backStack.push(entry)
        notifyBackStackListeners()
    }

    private fun notifyBackStackListeners() {
        val stackCopy = backStack.copy()
        backStackChangedListeners.forEach { it.invoke(stackCopy) }
    }

    companion object {
        private const val BACK_STACK_KEY = "FragmentNavigator container %d"
        const val ROUTE_TAG_DELIMITER = "-"
    }
}
