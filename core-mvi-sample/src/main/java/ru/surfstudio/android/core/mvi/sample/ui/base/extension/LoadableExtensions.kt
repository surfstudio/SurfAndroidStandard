package ru.surfstudio.android.core.mvi.sample.ui.base.extension

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.event.ResponseEvent
import ru.surfstudio.android.core.mvi.sample.domain.datalist.DataList
import ru.surfstudio.android.core.mvi.sample.ui.base.middleware.experimental.pagination.PaginationEvent
import ru.surfstudio.android.core.mvp.binding.rx.response.state.ResponseState
import ru.surfstudio.android.core.mvp.binding.rx.response.type.Response
import ru.surfstudio.android.core.mvp.binding.rx.extensions.Optional
import ru.surfstudio.android.core.mvp.binding.rx.extensions.toOptional
import ru.surfstudio.android.core.mvp.binding.rx.response.data.*
import ru.surfstudio.android.easyadapter.pagination.PaginationState
import ru.surfstudio.android.network.toObservable

/**
 * Маппинг функции для работы с данными
 *
 * TODO Перенести их в Template.
 * TODO Можно переопределить в проекте, чтобы задействовать свои стратегии работы с данными
 */
fun <T> ResponseState<T>.observeMainLoading() = observeLoading()
        .filter { it is MainLoading }
        .map { it.isLoading }

fun <T> ResponseState<T>.observeTransparentLoading() = observeLoading()
        .filter { it is TransparentLoading }
        .map { it.isLoading }

fun <T> ResponseState<T>.observeSwrLoading() = observeLoading()
        .filter { it is SwipeRefreshLoading }
        .map { it.isLoading }

fun <T> mapLoading(type: Response<T>, hasData: Boolean, isSwr: Boolean = false): Loading {
    val isLoading = type is Response.Loading
    return when {
        isSwr ->
            SwipeRefreshLoading(isLoading)
        hasData ->
            TransparentLoading(isLoading)
        else ->
            MainLoading(isLoading)
    }
}

fun <T> mapError(type: Response<T>, hasData: Boolean): Throwable =
        if (!hasData && type is Response.Error) {
            type.error
        } else {
            EmptyErrorException()
        }

fun <T> mapData(type: Response<T>, data: Optional<T>): Optional<T> =
        if (type is Response.Data) {
            type.data.toOptional()
        } else {
            data
        }

fun <T> mapDataList(
        type: Response<DataList<T>>,
        data: Optional<DataList<T>>,
        hasData: Boolean = data.hasValue
): Optional<DataList<T>> = if (type is Response.Data) {
    if (hasData && type.data.startPage > 1) { //мержим, если уже есть dataList + это не перезагрузка списка
        data.get().merge(type.data).toOptional()
    } else {
        type.data.toOptional()
    }
} else {
    data
}


fun <T, PE : PaginationEvent> ResponseEvent<DataList<T>>.toPaginationEvent(
        paginationEvent: PE,
        canGetMore: Boolean = type.canGetMore()
): Observable<PE> {
    return when (this.type) {
        is Response.Loading ->
            Observable.empty()
        is Response.Data -> paginationEvent
                .apply {
                    state = if (canGetMore) PaginationState.READY else PaginationState.COMPLETE
                }.toObservable()
        is Response.Error -> paginationEvent
                .apply { state = PaginationState.ERROR }.toObservable()
    }
}

inline fun <T, reified PE : PaginationEvent> ResponseEvent<DataList<T>>.toPaginationEvent(
        canGetMore: Boolean = type.canGetMore()
): Observable<PE> {
    val paginationEvent = PE::class.java.newInstance()
    return when (this.type) {
        is Response.Loading ->
            Observable.empty()
        is Response.Data -> paginationEvent
                .apply {
                    state = if (canGetMore) PaginationState.READY else PaginationState.COMPLETE
                }.toObservable()
        is Response.Error -> paginationEvent
                .apply { state = PaginationState.ERROR }.toObservable()
    }
}

fun <T> Response<DataList<T>>.canGetMore() = (this as? Response.Data)?.data?.canGetMore() == true

fun <T> ResponseState<DataList<T>>.canGetMore() = this.dataOrNull?.canGetMore() == true