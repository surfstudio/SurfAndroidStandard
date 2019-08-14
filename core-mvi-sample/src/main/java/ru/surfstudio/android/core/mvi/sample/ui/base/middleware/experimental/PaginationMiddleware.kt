package ru.surfstudio.android.core.mvi.sample.ui.base.middleware.experimental

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.event.PaginationEvent
import ru.surfstudio.android.core.mvi.event.ResponseEvent
import ru.surfstudio.android.core.mvi.sample.domain.datalist.DataList
import ru.surfstudio.android.core.mvi.ui.dsl.EventTransformerList
import ru.surfstudio.android.core.mvi.sample.ui.base.extension.toPaginationEvent
import ru.surfstudio.android.core.mvi.ui.middleware.RxMiddleware
import ru.surfstudio.android.core.mvi.util.filterIsInstance

/**
 * Middleware с поддержкой пагинации
 */
interface PaginationMiddleware<T : Event> : RxMiddleware<T> {

    fun <E> Observable<E>.mapPagination(
            paginationEvent: PaginationEvent,
            canGetMorePredicate: () -> Boolean

    ) = filterIsInstance<ResponseEvent<DataList<E>>>()
            .flatMap { it.toPaginationEvent(paginationEvent, canGetMore = canGetMorePredicate()) } as Observable<T>

    fun EventTransformerList<T>.mapPagination(
            paginationEvent: PaginationEvent,
            canGetMorePredicate: () -> Boolean
    ) = eventStream.mapPagination(paginationEvent, canGetMorePredicate)

}