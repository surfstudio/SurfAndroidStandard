package ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation.composition

import ru.surfstudio.android.core.ui.navigation.Route

/**
 * Rout that doesn't open any screen, simple stub.
 */
object NoScreenRoute : Route {
    override fun toString(): String = "NoScreenRoute"
}