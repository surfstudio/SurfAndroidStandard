package ru.surfstudio.android.navigation.navigator.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import ru.surfstudio.android.navigation.animation.Animations
import ru.surfstudio.android.navigation.animation.resource.BaseResourceAnimations
import ru.surfstudio.android.navigation.animation.resource.NoResourceAnimations
import ru.surfstudio.android.navigation.animation.set.SetAnimations
import ru.surfstudio.android.navigation.animation.shared.SharedElementAnimations
import ru.surfstudio.android.navigation.command.fragment.Add
import ru.surfstudio.android.navigation.command.NavigationCommand
import ru.surfstudio.android.navigation.command.fragment.Replace
import ru.surfstudio.android.navigation.backstack.fragment.FragmentBackStack
import ru.surfstudio.android.navigation.backstack.fragment.entry.FragmentBackStackEntry
import ru.surfstudio.android.navigation.backstack.fragment.entry.FragmentBackStackEntryObj
import ru.surfstudio.android.navigation.backstack.fragment.listener.BackStackChangedListener
import ru.surfstudio.android.navigation.backstack.fragment.BackStackFragmentRoute
import ru.surfstudio.android.navigation.route.fragment.FragmentRoute
import ru.surfstudio.android.navigation.utils.FragmentAnimationSupplier

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

    init {
        onRestoreState(savedState)
    }

    protected val backStack: FragmentBackStack = FragmentBackStack()

    override val backStackEntryCount: Int
        get() = backStack.size

    private val backStackChangedListeners = arrayListOf<BackStackChangedListener>()

    protected open var animationSupplier = FragmentAnimationSupplier()

    override fun add(route: FragmentRoute, animations: Animations) {
        val fragmentManager = fragmentManager
        fragmentManager.executePendingTransactions()

        val backStackTag = convertToBackStackTag(route.getTag())
        val fragment = route.createFragment()

        fragmentManager.beginTransaction().apply {
            supplyWithAnimations(animations)
            add(containerId, fragment, backStackTag)
            addToBackStack(
                    FragmentBackStackEntry(
                            backStackTag,
                            fragment,
                            Add(BackStackFragmentRoute(route.getTag()), animations)
                    )
            )
            commit()
        }
    }


    override fun replace(route: FragmentRoute, animations: Animations) {
        val fragmentManager = fragmentManager
        fragmentManager.executePendingTransactions()

        val backStackTag = convertToBackStackTag(route.getTag())
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
                            Replace(BackStackFragmentRoute(route.getTag()), animations)
                    )
            )
            commit()
        }
    }

    override fun remove(route: FragmentRoute, animations: Animations): Boolean {
        val fragmentManager = fragmentManager
        fragmentManager.executePendingTransactions()

        val fragment = findFragment(convertToBackStackTag(route.getTag())) ?: return false

        fragmentManager.beginTransaction()
                .supplyWithAnimations(animations)
                .remove(fragment)
                .commit()
        return true
    }

    private fun findFragment(backStackTag: String): Fragment? =
            backStack.findFragment(backStackTag)

    override fun show(route: FragmentRoute, animations: Animations): Boolean {
        return toggleVisibility(route, true, animations)
    }

    override fun hide(route: FragmentRoute, animations: Animations): Boolean {
        return toggleVisibility(route, false, animations)
    }

    override fun removeLast(animations: Animations): Boolean {
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

    override fun replaceHard(route: FragmentRoute, animations: Animations) {
        val fragmentManager = fragmentManager
        fragmentManager.executePendingTransactions()

        val backStackTag = convertToBackStackTag(route.getTag())
        val fragment = route.createFragment()

        fragmentManager.beginTransaction().apply {
            supplyWithAnimations(animations)
            remove(backStack.pop().fragment)
            add(containerId, fragment, backStackTag)
            addToBackStack(
                    FragmentBackStackEntry(
                            backStackTag,
                            fragment,
                            Replace(BackStackFragmentRoute(route.getTag()), animations)
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
    ): Boolean {
        val fragmentManager = fragmentManager
        fragmentManager.executePendingTransactions()

        val fragment = fragmentManager.findFragmentByTag(convertToBackStackTag(route.getTag()))
                ?: return false

        fragmentManager.beginTransaction()
                .apply {
                    supplyWithAnimations(animationBundle)
                    if (shouldShow) show(fragment) else hide(fragment)
                    commit()
                }

        return true
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
