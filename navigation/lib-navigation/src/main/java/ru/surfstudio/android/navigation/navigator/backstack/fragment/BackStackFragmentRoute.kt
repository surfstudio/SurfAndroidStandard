package ru.surfstudio.android.navigation.navigator.backstack.fragment

import ru.surfstudio.android.navigation.route.fragment.FragmentRoute
import java.io.Serializable

/**
 * Simple route with meta information about a screen to be placed in a back stack.
 *
 * Serializable to survive configuration changes.
 */
data class BackStackFragmentRoute(val backStackTag: String) : FragmentRoute(), Serializable
