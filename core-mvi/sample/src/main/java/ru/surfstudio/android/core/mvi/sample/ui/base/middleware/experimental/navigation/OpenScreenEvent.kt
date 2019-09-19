package ru.surfstudio.android.core.mvi.sample.ui.base.middleware.experimental.navigation

import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.sample.ui.base.middleware.experimental.ExperimentalFeature
import ru.surfstudio.android.core.ui.navigation.Route

/**
 * [Event] открытия экрана
 */
@ExperimentalFeature
interface OpenScreenEvent : Event {
    val route: Route
}