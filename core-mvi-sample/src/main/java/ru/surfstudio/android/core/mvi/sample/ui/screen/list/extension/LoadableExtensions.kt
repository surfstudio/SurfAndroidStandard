package ru.surfstudio.android.core.mvi.sample.ui.screen.list.extension

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.event.LoadableEvent
import ru.surfstudio.android.core.mvi.sample.domain.datalist.DataList
import ru.surfstudio.android.core.mvi.sample.ui.base.pagination.PaginationEvent
import ru.surfstudio.android.core.mvp.binding.rx.loadable.state.LoadableState
import ru.surfstudio.android.core.mvp.binding.rx.loadable.type.LoadType
import ru.surfstudio.android.core.mvp.binding.rx.extensions.Optional
import ru.surfstudio.android.core.mvp.binding.rx.extensions.asOptional
import ru.surfstudio.android.core.mvp.binding.rx.loadable.data.*
import ru.surfstudio.android.easyadapter.pagination.PaginationState
import ru.surfstudio.android.network.toObservable

/**
 * Маппинг функции для работы с данными
 *
 * TODO Перенести их в Template.
 * TODO Можно переопределить в проекте, чтобы задействовать свои стратегии работы с данными
 */
fun <T> LoadableState<T>.observeMainLoading() = observeLoading()
        .filter { it is MainLoading }
        .map { it.isLoading }

fun <T> LoadableState<T>.observeTransparentLoading() = observeLoading()
        .filter { it is TransparentLoading }
        .map { it.isLoading }

fun <T> LoadableState<T>.observeSwrLoading() = observeLoading()
        .filter { it is SwipeRefreshLoading }
        .map { it.isLoading }

fun <T> mapLoading(type: LoadType<T>, hasData: Boolean, isSwr: Boolean = false): Loading {
    val isLoading = type is LoadType.Loading
    return when {
        isSwr ->
            SwipeRefreshLoading(isLoading)
        hasData ->
            TransparentLoading(isLoading)
        else ->
            MainLoading(isLoading)
    }
}

fun <T> mapError(type: LoadType<T>, hasData: Boolean): Throwable =
        if (!hasData && type is LoadType.Error) {
            type.error
        } else {
            EmptyErrorException()
        }

fun <T> mapData(type: LoadType<T>, data: Optional<T>): Optional<T> =
        if (type is LoadType.Data) {
            type.data.asOptional()
        } else {
            data
        }

fun <T> mapDataList(
        type: LoadType<DataList<T>>,
        data: Optional<DataList<T>>,
        hasData: Boolean = data.hasValue
): Optional<DataList<T>> = if (type is LoadType.Data) {
    if (hasData && type.data.startPage > 0) { //мержим, если уже есть dataList + это не перезагрузка списка
        data.get().merge(type.data).asOptional()
    } else {
        type.data.asOptional()
    }
} else {
    data
}


fun <T, PE : PaginationEvent> LoadableEvent<DataList<T>>.toPaginationEvent(
        paginationEvent: PE,
        canGetMore: Boolean = type.canGetMore()
): Observable<PE> {
    return when (this.type) {
        is LoadType.Loading ->
            Observable.empty()
        is LoadType.Data -> paginationEvent
                .apply {
                    state = if (canGetMore) PaginationState.READY else PaginationState.COMPLETE
                }.toObservable()
        is LoadType.Error -> paginationEvent
                .apply { state = PaginationState.ERROR }.toObservable()
    }
}

fun <T> LoadType<DataList<T>>.canGetMore() = (this as? LoadType.Data)?.data?.canGetMore() == true

fun <T> LoadableState<DataList<T>>.canGetMore() = this.dataOrNull?.canGetMore() == true