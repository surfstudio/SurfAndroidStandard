package ru.surfstudio.android.navigation.navigator.fragment.tab.host

import ru.surfstudio.android.navigation.navigator.fragment.FragmentNavigator

/**
 * List of [TabHostEntries]
 */
class TabHostEntries : MutableList<TabHostEntry> by ArrayList() {

    val tags: List<String> get() = map(TabHostEntry::tag)
    val navigators: List<FragmentNavigator> get() = map(TabHostEntry::navigator)
}