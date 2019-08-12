package ru.surfstudio.android.core.mvi.sample.ui.base.pagination

import ru.surfstudio.android.easyadapter.pagination.PaginationState

/**
 * Событие пагинации
 */
interface PaginationEvent {
    var state: PaginationState
}