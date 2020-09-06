package ru.surfstudio.android.navigation.navigator.fragment.tab

import ru.surfstudio.android.navigation.navigator.fragment.FragmentNavigatorInterface
import ru.surfstudio.android.navigation.navigator.fragment.tab.listener.ActiveTabReopenedListener
import ru.surfstudio.android.navigation.navigator.fragment.tab.listener.TabHeadChangedListener
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
interface TabFragmentNavigatorInterface : FragmentNavigatorInterface {

    val tabCount: Int

    fun setActiveTabReopenedListener(listener: ActiveTabReopenedListener?)

    fun addTabHeadChangedListener(listener: TabHeadChangedListener)

    fun removeTabHeadChangedListener(listener: TabHeadChangedListener)
}