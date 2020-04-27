package ru.surfstudio.android.core.mvi.sample.ui.base.extension

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.event.RequestEvent
import ru.surfstudio.android.core.mvi.sample.ui.base.middleware.experimental.pagination.PaginationEvent
import ru.surfstudio.android.core.mvp.binding.rx.request.state.RequestState
import ru.surfstudio.android.core.mvp.binding.rx.request.Request
import ru.surfstudio.android.datalistpagecount.domain.datalist.DataList
import ru.surfstudio.android.pagination.PaginationState
import ru.surfstudio.android.rx.extension.toObservable

fun <T, PE : PaginationEvent> RequestEvent<DataList<T>>.toPaginationEvent(
        paginationEvent: PE,
        canGetMore: Boolean = request.canGetMore()
): Observable<PE> {
    return when (this.request) {
        is Request.Loading ->
            Observable.empty()
        is Request.Success -> paginationEvent
                .apply {
                    state = if (canGetMore) PaginationState.READY else PaginationState.COMPLETE
                }.toObservable()
        is Request.Error -> paginationEvent
                .apply { state = PaginationState.ERROR }.toObservable()
    }
}

inline fun <T, reified PE : PaginationEvent> RequestEvent<DataList<T>>.toPaginationEvent(
        canGetMore: Boolean = request.canGetMore()
): Observable<PE> {
    val paginationEvent = PE::class.java.newInstance()
    return when (this.request) {
        is Request.Loading ->
            Observable.empty()
        is Request.Success -> paginationEvent
                .apply {
                    state = if (canGetMore) PaginationState.READY else PaginationState.COMPLETE
                }.toObservable()
        is Request.Error -> paginationEvent
                .apply { state = PaginationState.ERROR }.toObservable()
    }
}

fun <T> Request<DataList<T>>.canGetMore() = getDataOrNull()?.canGetMore() ?: false

fun <T> RequestState<DataList<T>>.canGetMore() = this.data?.canGetMore() ?: false