package ru.surfstudio.android.core.mvi.event

import ru.surfstudio.android.easyadapter.pagination.PaginationState
/**
 * Событие пагинации
 */
interface PaginationEvent {
    var state: PaginationState
}