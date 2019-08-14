package ru.surfstudio.android.core.mvi.event

import ru.surfstudio.android.core.ui.navigation.Route
import ru.surfstudio.android.core.ui.navigation.ScreenResult
import ru.surfstudio.android.easyadapter.pagination.PaginationState
import java.io.Serializable

/**
 * [Event] открытия экрана
 */
interface OpenScreenEvent : Event {
    val route: Route
}

interface ScreenResultEvent<T : Serializable> : Event {
    var result: ScreenResult<T>
}

interface BaseSwipeRefreshEvent

interface BaseLoadNextPageEvent

/**
 * Событие пагинации
 */
interface PaginationEvent {
    var state: PaginationState
}