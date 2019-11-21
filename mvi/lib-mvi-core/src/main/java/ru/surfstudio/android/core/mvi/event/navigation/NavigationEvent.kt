package ru.surfstudio.android.core.mvi.event.navigation

import ru.surfstudio.android.core.mvi.event.Event
import java.io.Serializable

/**
 * Base navigation event.
 *
 * Implements [Serializable] for passing it to the route.
 */
interface NavigationEvent : Event, Serializable