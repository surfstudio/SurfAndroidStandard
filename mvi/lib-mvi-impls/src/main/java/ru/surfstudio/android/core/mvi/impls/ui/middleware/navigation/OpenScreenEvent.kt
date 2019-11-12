package ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation

import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.ui.navigation.Route

/**
 * [Event] открытия экрана
 */
interface OpenScreenEvent : NavigationEvent {
    val route: Route
}