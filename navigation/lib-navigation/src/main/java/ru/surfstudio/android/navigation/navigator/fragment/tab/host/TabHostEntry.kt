package ru.surfstudio.android.navigation.navigator.fragment.tab.host

/**
 * Entry that contains information about fragment bab.
 *
 * @param tag tag bound to the fragment
 * @param navigator navigator which is used to control tab stack
 */
data class TabHostEntry(
        val tag: String,
        val navigator: TabHostFragmentNavigator
)