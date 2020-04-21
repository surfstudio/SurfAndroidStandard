package ru.surfstudio.android.navigation.navigator.fragment.tab

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import ru.surfstudio.android.navigation.animation.Animations
import ru.surfstudio.android.navigation.animation.resource.EmptyResourceAnimations
import ru.surfstudio.android.navigation.command.fragment.Replace
import ru.surfstudio.android.navigation.backstack.fragment.listener.BackStackChangedListener
import ru.surfstudio.android.navigation.navigator.fragment.FragmentNavigatorInterface
import ru.surfstudio.android.navigation.navigator.fragment.tab.host.TabHostEntries
import ru.surfstudio.android.navigation.navigator.fragment.tab.host.TabHostEntry
import ru.surfstudio.android.navigation.navigator.fragment.tab.host.TabHostFragmentNavigator
import ru.surfstudio.android.navigation.navigator.fragment.tab.listener.ActiveTabReopenedListener
import ru.surfstudio.android.navigation.route.tab.TabRootRoute
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
        protected val fragmentManager: FragmentManager,
        protected val containerId: Int,
        savedState: Bundle? = null
) : TabFragmentNavigatorInterface {

    private var activeTabTag: String = ""

    private val hostEntries: TabHostEntries = TabHostEntries()

    private val activeNavigator
        get() = hostEntries.first { it.tag == activeTabTag }.navigator

    private var activeTabReopenedListener: ActiveTabReopenedListener = {}

    override val backStackEntryCount: Int
        get() = activeNavigator.backStackEntryCount

    override val tabCount: Int
        get() = hostEntries.size

    init {
        onRestoreState(savedState)
    }

    override fun add(route: FragmentRoute, animations: Animations) {
        if (route is TabRootRoute) {
            openTab(route)
        } else {
            activeNavigator.add(route, animations)
        }
    }

    override fun replace(route: FragmentRoute, animations: Animations) {
        if (route is TabRootRoute) {
            openTab(route)
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

    override fun removeUntil(route: FragmentRoute, isInclusive: Boolean) {
        activeNavigator.removeUntil(route, isInclusive)
    }

    override fun removeAll() {
        activeNavigator.removeAll()
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

    override fun addBackStackChangeListener(listener: BackStackChangedListener) {
        activeNavigator.addBackStackChangeListener(listener)
    }

    override fun removeBackStackChangeListener(listener: BackStackChangedListener) {
        activeNavigator.removeBackStackChangeListener(listener)
    }

    override fun setActiveTabReopenedListener(listener: ActiveTabReopenedListener) {
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

        val newNavigator = TabHostFragmentNavigator(fragmentManager, containerId, routeTag)
        val newEntry = TabHostEntry(routeTag, newNavigator)
        hostEntries.add(newEntry)

        fragmentManager.beginTransaction()
                .detachVisibleTabs()
                .commitNow()
        newNavigator.replace(route, EmptyResourceAnimations)
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
        fragmentManager.beginTransaction().apply {
            detachVisibleTabs()
            extractFragmentsToAttach().forEach { attach(it) }
            commitNow()
        }
    }

    /**
     * Извлечение из бекстека фрагментов, которые необходимо присоединить к отображению.
     *
     * Просто извлечь последний видимый не получится,
     * так как фрагменты могут быть добавлены с помощью операции [Add],
     * и тогда нам необходимо приаттачить все фрагменты до первого, у которого вызвана операция [Replace]
     */
    private fun extractFragmentsToAttach(): List<Fragment> {
        val activeStack = activeNavigator.obtainBackStack()
        val activeStackSize = activeStack.size
        val fragmentsToAttach = mutableListOf<Fragment>()
        var i = 0
        do {
            i++
            val index = activeStackSize - i
            fragmentsToAttach.add(activeStack[index].fragment)
        } while (activeStack[index].command !is Replace)
        return fragmentsToAttach.reversed()
    }

    private fun FragmentTransaction.detachVisibleTabs(): FragmentTransaction {
        hostEntries.forEach { entry ->
            if (entry.tag != activeTabTag) {
                entry.navigator.obtainBackStack().forEach { detach(it.fragment) }
            }
        }
        return this
    }

    companion object {
        const val EXTRA_HOST_TAGS = "host_tags"
        const val EXTRA_ACTIVE_TAG = "active_tag"
    }
}
