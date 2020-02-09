package ru.surfstudio.android.navigation.navigator.tab.host

/**
 * List of [TabHostEntries]
 */
class TabHostEntries : MutableList<TabHostEntry> by ArrayList() {

    val tags: List<String> get() = map(TabHostEntry::tag)
    val fragments: List<TabHostFragment> get() = map(TabHostEntry::fragment)
}