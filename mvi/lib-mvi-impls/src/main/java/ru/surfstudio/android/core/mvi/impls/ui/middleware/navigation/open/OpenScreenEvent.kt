package ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation.open

import ru.surfstudio.android.core.mvi.event.navigation.NavigationEvent
import ru.surfstudio.android.core.ui.navigation.Route

/**
 * [NavigationEvent] that opens screen with specific [route]
 */
interface OpenScreenEvent : NavigationEvent {
    val route: Route
}