package ru.surfstudio.android.navigation.route.stub

import ru.surfstudio.android.navigation.route.fragment.FragmentRoute
import ru.surfstudio.android.navigation.route.tab.TabRoute

/**
 * Stub route just for executing fragment command with tab navigator.
 *
 * Does not contain any information about activity, data or class, and simply used to indicate
 * that particular command should be executed with activity screen.
 */
object StubTabFragmentRoute : FragmentRoute(), TabRoute