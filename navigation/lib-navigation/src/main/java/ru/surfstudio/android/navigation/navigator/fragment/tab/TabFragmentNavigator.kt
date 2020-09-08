package ru.surfstudio.android.navigation.navigator.fragment.tab

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import ru.surfstudio.android.navigation.animation.Animations
import ru.surfstudio.android.navigation.animation.DefaultAnimations
import ru.surfstudio.android.navigation.animation.utils.FragmentAnimationSupplier
import ru.surfstudio.android.navigation.backstack.fragment.listener.FragmentBackStackChangedListener
import ru.surfstudio.android.navigation.navigator.fragment.FragmentNavigatorInterface
import ru.surfstudio.android.navigation.navigator.fragment.tab.host.TabHostEntries
import ru.surfstudio.android.navigation.navigator.fragment.tab.host.TabHostEntry
import ru.surfstudio.android.navigation.navigator.fragment.tab.host.TabHostFragmentNavigator
import ru.surfstudio.android.navigation.navigator.fragment.tab.listener.ActiveTabReopenedListener
import ru.surfstudio.android.navigation.navigator.fragment.tab.listener.TabHeadChangedListener
import ru.surfstudio.android.navigation.route.fragment.FragmentRoute
import ru.surfstudio.android.navigation.route.tab.TabHeadRoute

/**
 * Navigator which supports tabs with their own stacks.
 * Can be used in Bottom Navigation mechanism.
 *
 * It works with populating tabs lazily: you don't need to specify it's size or tabs at the initialization.
 * Instead of that, you must dynamically [add] routes with class [TabHeadRoute] and the tabs will be switched automatically.
 *
 * Delegates all the methods of a [FragmentNavigatorInterface] to an active stack.
 */
open class TabFragmentNavigator(
        protected val fragmentManager: FragmentManager,
        protected val containerId: Int,
        savedState: Bundle? = null
) : TabFragmentNavigatorInterface {

    private var activeTabTag: String = ""

    private val hostEntries: TabHostEntries = TabHostEntries()

    private val activeNavigator
        get() = obtainCurrentNavigator()

    private var activeTabReopenedListener: ActiveTabReopenedListener? = null
    private val tabHeadChangedListeners: MutableList<TabHeadChangedListener> = mutableListOf()

    override val backStackEntryCount: Int
        get() = activeNavigator.backStackEntryCount

    override val tabCount: Int
        get() = hostEntries.size

    protected open val animationSupplier = FragmentAnimationSupplier()

    init {
        onRestoreState(savedState)
    }

    override fun add(route: FragmentRoute, animations: Animations) {
        if (route is TabHeadRoute) {
            openHead(route)
        } else {
            activeNavigator.add(route, animations)
        }
    }

    override fun replace(route: FragmentRoute, animations: Animations) {
        if (route is TabHeadRoute) {
            openHead(route)
        } else {
            activeNavigator.replace(route, animations)
        }
    }

    override fun replaceHard(route: FragmentRoute, animations: Animations) {
        activeNavigator.replaceHard(route, animations)
    }

    override fun remove(route: FragmentRoute, animations: Animations) {
        activeNavigator.remove(route, animations)
    }

    override fun removeLast(animations: Animations) {
        activeNavigator.removeLast(animations)
    }

    override fun removeUntil(route: FragmentRoute, animations: Animations, isInclusive: Boolean) {
        activeNavigator.removeUntil(route, animations, isInclusive)
    }

    override fun removeAll(animations: Animations, shouldRemoveLast: Boolean) {
        activeNavigator.removeAll(animations, shouldRemoveLast)
    }

    override fun hide(route: FragmentRoute, animations: Animations) {
        activeNavigator.hide(route, animations)
    }

    override fun show(route: FragmentRoute, animations: Animations) {
        activeNavigator.show(route, animations)
    }

    final override fun onSaveState(outState: Bundle?) {
        outState ?: return
        outState.putString(EXTRA_ACTIVE_TAG, activeTabTag)
        outState.putStringArrayList(EXTRA_HOST_TAGS, ArrayList(hostEntries.tags))
        hostEntries.navigators.forEach { it.onSaveState(outState) }
    }

    final override fun onRestoreState(savedInstanceState: Bundle?) {
        savedInstanceState ?: return

        hostEntries.clear()

        val tags = savedInstanceState.getStringArrayList(EXTRA_HOST_TAGS) ?: return
        val navigators = tags.map { TabHostFragmentNavigator(fragmentManager, containerId, it) }
        tags.forEachIndexed { index, s -> hostEntries.add(TabHostEntry(s, navigators[index])) }

        hostEntries.navigators.forEach { it.onRestoreState(savedInstanceState) }

        activeTabTag = savedInstanceState.getString(EXTRA_ACTIVE_TAG) ?: ""
    }

    override fun addBackStackChangeListener(listener: FragmentBackStackChangedListener) {
        activeNavigator.addBackStackChangeListener(listener)
    }

    override fun removeBackStackChangeListener(listener: FragmentBackStackChangedListener) {
        activeNavigator.removeBackStackChangeListener(listener)
    }

    override fun setActiveTabReopenedListener(listener: ActiveTabReopenedListener?) {
        activeTabReopenedListener = listener
    }

    override fun addTabHeadChangedListener(listener: TabHeadChangedListener) {
        tabHeadChangedListeners.add(listener)
    }

    override fun removeTabHeadChangedListener(listener: TabHeadChangedListener) {
        tabHeadChangedListeners.add(listener)
    }

    /**
     * Opens tab as a head.
     */
    fun openHead(route: FragmentRoute) {
        if (hostEntries.tags.contains(route.getId())) {
            openExistentTab(route.getId())
        } else {
            addNewTab(route)
        }
        tabHeadChangedListeners.forEach { it.invoke(route) }
    }

    protected open fun addNewTab(route: FragmentRoute) {
        val routeTag = route.getId()
        activeTabTag = routeTag

        val newNavigator = TabHostFragmentNavigator(fragmentManager, containerId, routeTag)
        val newEntry = TabHostEntry(routeTag, newNavigator)
        hostEntries.add(newEntry)

        fragmentManager.beginTransaction().apply {
            detachVisibleTabs()
            commitNow()
        }
        newNavigator.replace(route, DefaultAnimations.tab)
    }

    protected open fun openExistentTab(routeTag: String) {
        if (activeTabTag == routeTag) {
            activeTabReopenedListener?.invoke(routeTag)
        } else {
            attachExistentTab(routeTag)
        }
    }

    protected open fun attachExistentTab(routeTag: String) {
        activeTabTag = routeTag
        fragmentManager.beginTransaction().apply {
            animationSupplier.supplyWithAnimations(this, DefaultAnimations.tab)
            detachVisibleTabs()
            activeNavigator.findLastVisibleFragments().forEach { attach(it) }
            commitNow()
        }
    }

    private fun FragmentTransaction.detachVisibleTabs(): FragmentTransaction {
        hostEntries.forEach { entry ->
            if (entry.tag != activeTabTag) {
                entry.navigator.obtainBackStack().forEach { detach(it.fragment) }
            }
        }
        return this
    }

    private fun obtainCurrentNavigator(): TabHostFragmentNavigator {
        val hostEntry = hostEntries.firstOrNull { it.tag == activeTabTag } ?: error(NO_HEAD_ERROR)
        return hostEntry.navigator
    }

    companion object {
        const val EXTRA_HOST_TAGS = "host_tags"
        const val EXTRA_ACTIVE_TAG = "active_tag"

        const val NO_HEAD_ERROR = "Your TabFragmentNavigator doesn't have head! " +
                "You need to inherit your route from TabHeadRoute if it needs to be the main element in tab"
    }
}
