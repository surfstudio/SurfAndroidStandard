package ru.surfstudio.android.core.mvi.event

import ru.surfstudio.android.core.ui.navigation.Route
import ru.surfstudio.android.easyadapter.pagination.PaginationState

/**
 * [Event] открытия экрана
 */
interface OpenScreenEvent : Event {
    val route: Route
}

interface BaseSwipeRefreshEvent

interface BaseLoadNextPageEvent

/**
 * Событие пагинации
 */
interface PaginationEvent {
    var state: PaginationState
}