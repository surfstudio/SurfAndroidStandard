package ru.surfstudio.android.navigation.navigator.tab

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import ru.surfstudio.android.navigation.animation.BaseScreenAnimations
import ru.surfstudio.android.navigation.navigator.backstack.fragment.listener.BackStackChangedListener
import ru.surfstudio.android.navigation.navigator.fragment.FragmentNavigatorInterface
import ru.surfstudio.android.navigation.navigator.tab.host.TabHostEntries
import ru.surfstudio.android.navigation.navigator.tab.host.TabHostEntry
import ru.surfstudio.android.navigation.navigator.tab.host.TabHostFragment
import ru.surfstudio.android.navigation.navigator.tab.listener.ActiveTabReopenedListener
import ru.surfstudio.android.navigation.navigator.tab.route.TabRootRoute
import ru.surfstudio.android.navigation.route.fragment.FragmentRoute

/**
 * Navigator which supports tabs with their own stacks.
 * Can be used in Bottom Navigation mechanism.
 *
 * It works with populating tabs lazily: you don't need to specify it's size or tabs at the initialization.
 * Instead of that, you must dynamically [add] routes with class [TabRootRoute] and the tabs will be switched automatically.
 *
 * Delegates all the methods of a [FragmentNavigatorInterface] to an active stack.
 */
open class TabFragmentNavigator(
        override val fragmentManager: FragmentManager,
        override val containerId: Int = View.NO_ID
) : FragmentNavigatorInterface {


    private var activeTabTag: String = ""

    private val hostEntries: TabHostEntries = TabHostEntries()


    private val activeFragment: TabHostFragment
        get() = hostEntries.fragments.firstOrNull { it.tag == activeTabTag }
                ?: hostEntries.fragments.first()

    private val activeNavigator
        get() = activeFragment.navigator

    private var activeTabReopenedListener: ActiveTabReopenedListener = {}

    override val backStackEntryCount: Int
        get() = activeNavigator.backStackEntryCount

    override fun add(route: FragmentRoute, animations: BaseScreenAnimations) {
        if (route is TabRootRoute) {
            openTab(route)
        } else {
            activeNavigator.add(route, animations) // TODO try to use common navigation algorythm on empty navigation stack
        }
    }

    override fun replace(route: FragmentRoute, animations: BaseScreenAnimations) {
        activeNavigator.replace(route, animations)
    }

    override fun replaceHard(route: FragmentRoute, animations: BaseScreenAnimations) {
        activeNavigator.replaceHard(route, animations)
    }

    override fun remove(route: FragmentRoute, animations: BaseScreenAnimations): Boolean {
        return activeNavigator.remove(route, animations)
    }

    override fun removeLast(animations: BaseScreenAnimations): Boolean {
        return activeNavigator.removeLast(animations)
    }

    override fun removeUntil(route: FragmentRoute, isInclusive: Boolean): Boolean {
        return activeNavigator.removeUntil(route, isInclusive)
    }

    override fun removeAll(): Boolean {
        return activeNavigator.removeAll()
    }

    override fun hide(route: FragmentRoute, animations: BaseScreenAnimations): Boolean {
        return activeNavigator.hide(route, animations)
    }

    override fun show(route: FragmentRoute, animations: BaseScreenAnimations): Boolean {
        return activeNavigator.show(route, animations)
    }

    override fun onSaveState(outState: Bundle?) {
        outState ?: return
        outState.putString(EXTRA_ACTIVE_TAG, activeTabTag)
        outState.putStringArrayList(EXTRA_HOST_TAGS, ArrayList(hostEntries.tags))
        hostEntries.fragments.forEach { it.navigator.onSaveState(outState) }
    }

    override fun onRestoreState(savedInstanceState: Bundle?) {
        savedInstanceState ?: return

        hostEntries.clear()
        val restoredTags = savedInstanceState.getStringArrayList(EXTRA_HOST_TAGS) ?: arrayListOf()
        val restoredEntries = restoredTags
                .mapNotNull { tag ->
                    val fragment = fragmentManager.findFragmentByTag(tag) as? TabHostFragment
                    fragment?.let { TabHostEntry(tag, fragment) }
                }
        hostEntries.addAll(restoredEntries)
        hostEntries.fragments.forEach { it.navigator.onRestoreState(savedInstanceState) }

        activeTabTag = savedInstanceState.getString(EXTRA_ACTIVE_TAG) ?: ""
        attachExistentTab(activeTabTag)
    }

    fun addBackStackChangeListener(listener: BackStackChangedListener) {
        activeNavigator.addBackStackChangeListener(listener)
    }

    fun removeBackStackChangeListener(listener: BackStackChangedListener) {
        activeNavigator.removeBackStackChangeListener(listener)
    }

    fun setActiveTabReopenedListener(listener: ActiveTabReopenedListener) {
        activeTabReopenedListener = listener
    }

    /**
     * Opens tab as a root.
     */
    fun openTab(route: FragmentRoute) {
        if (hostEntries.tags.contains(route.getTag())) {
            openExistentTab(route.getTag())
        } else {
            addNewTab(route)
        }
    }

    private fun addNewTab(route: FragmentRoute) {
        val routeTag = route.getTag()
        activeTabTag = routeTag
        fragmentManager.executePendingTransactions()
        fragmentManager.beginTransaction().apply {
            val newStackFragment = TabHostFragment.newInstance(route)
            detachVisibleTabs()
            hostEntries.add(TabHostEntry(routeTag, newStackFragment))
            add(containerId, newStackFragment, routeTag)
            commitNow()
        }
    }

    private fun openExistentTab(routeTag: String) {
        if (activeTabTag == routeTag) {
            activeTabReopenedListener(routeTag)
        } else {
            attachExistentTab(routeTag)
        }
    }

    private fun attachExistentTab(routeTag: String) {
        activeTabTag = routeTag
        fragmentManager.executePendingTransactions()
        fragmentManager.beginTransaction().apply {
            detachVisibleTabs()
            attach(activeFragment)
            commitNow()
        }
    }

    private fun FragmentTransaction.detachVisibleTabs() {
        hostEntries.fragments.forEach {
            if (!it.isDetached && it.tag != activeTabTag) detach(it)
        }
    }

    companion object {
        const val EXTRA_HOST_TAGS = "host_tags"
        const val EXTRA_ACTIVE_TAG = "active_tag"
    }
}
