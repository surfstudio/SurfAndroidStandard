package ru.surfstudio.android.navigation.navigator.fragment.tab.listener

/**
 * Listener that invokes after active tab of
 * [ru.surfstudio.android.navigation.navigator.fragment.tab.TabFragmentNavigator] being reopened again.
 *
 * @param tag tag associated with active tab.
 */
typealias ActiveTabReopenedListener = (tag: String) -> Unit