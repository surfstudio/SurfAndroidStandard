package ru.surfstudio.android.core.mvi.event

import ru.surfstudio.android.core.ui.navigation.Route

/**
 * [Event] открытия экрана
 */
interface OpenScreenEvent : Event {
    val route: Route
}