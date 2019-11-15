package ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation.composition

import ru.surfstudio.android.core.mvi.event.composition.CompositionEvent
import ru.surfstudio.android.core.mvi.event.navigation.NavigationEvent
import java.io.Serializable

/**
 * [CompositionEvent] for simplifying navigation process.
 *
 * Implements [Serializable] for passing it to the route.
 *
 * [NavigationComposition] should be decomposed in [NavigationMiddleware].
 */
interface NavigationComposition : CompositionEvent<NavigationEvent>, Serializable