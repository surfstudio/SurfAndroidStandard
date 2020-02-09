package ru.surfstudio.android.navigation.navigator.tab.host

/**
 * Entry that contains information about [TabHostFragment].
 *
 * @param tag tag bound to the fragment
 * @param fragment fragment which holds tab stack
 */
data class TabHostEntry(
        val tag: String,
        val fragment: TabHostFragment
)