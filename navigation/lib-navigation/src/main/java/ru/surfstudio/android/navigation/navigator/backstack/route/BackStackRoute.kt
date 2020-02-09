package ru.surfstudio.android.navigation.navigator.backstack.route

import ru.surfstudio.android.navigation.route.Route
import java.io.Serializable

/**
 * Simple route with meta information about a screen to be placed in a back stack.
 *
 * Serializable to survive configuration changes.
 */
data class BackStackRoute(val tag: String) : Route, Serializable
