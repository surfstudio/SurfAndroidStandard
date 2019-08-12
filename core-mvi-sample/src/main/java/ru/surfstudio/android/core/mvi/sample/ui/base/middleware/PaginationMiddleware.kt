package ru.surfstudio.android.core.mvi.sample.ui.base.middleware

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.event.LoadableEvent
import ru.surfstudio.android.core.mvi.sample.domain.datalist.DataList
import ru.surfstudio.android.core.mvi.sample.ui.base.pagination.PaginationEvent
import ru.surfstudio.android.core.mvi.sample.ui.screen.list.EventTransformerList
import ru.surfstudio.android.core.mvi.sample.ui.screen.list.extension.toPaginationEvent
import ru.surfstudio.android.core.mvi.ui.middleware.RxMiddleware
import ru.surfstudio.android.core.mvi.util.filterIsInstance

interface PaginationMiddleware<T : Event> : RxMiddleware<T> {

    fun <E> Observable<E>.mapPagination(
            paginationEvent: PaginationEvent,
            canGetMorePredicate: () -> Boolean

    ) = filterIsInstance<LoadableEvent<DataList<E>>>()
            .flatMap { it.toPaginationEvent(paginationEvent, canGetMore = canGetMorePredicate()) } as Observable<T>

    fun EventTransformerList<T>.mapPagination(
            paginationEvent: PaginationEvent,
            canGetMorePredicate: () -> Boolean
    ) = eventStream.mapPagination(paginationEvent, canGetMorePredicate)

}