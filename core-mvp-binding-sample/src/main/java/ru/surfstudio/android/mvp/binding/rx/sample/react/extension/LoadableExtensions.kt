package ru.surfstudio.android.mvp.binding.rx.sample.react.extension

import ru.surfstudio.android.core.mvp.binding.react.loadable.event.LoadType
import ru.surfstudio.android.core.mvp.binding.react.loadable.LoadableState
import ru.surfstudio.android.core.mvp.binding.react.loadable.data.*
import ru.surfstudio.android.core.mvp.binding.react.optional.Optional
import ru.surfstudio.android.core.mvp.binding.react.optional.asOptional
import ru.surfstudio.android.mvp.binding.rx.sample.easyadapter.domain.datalist.DataList

/**
 * Маппинг функции для работы с данными
 *
 * TODO Перенести их в Template.
 * TODO Можно переопределить в проекте, чтобы задействовать свои стратегии работы с данными
 */

fun <T> mapLoading(type: LoadType<T>, hasData: Boolean, isSwr: Boolean = false): Loading {
    val isLoading = type is LoadType.Loading
    return if (hasData) {
        if (isSwr) {
            SwipeRefreshLoading(isLoading)
        } else {
            TransparentLoading(isLoading)
        }
    } else {
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

fun <T, L : DataList<T>> mapDataList(
        type: LoadType<L>,
        data: Optional<L>,
        hasData: Boolean = data.hasValue
) = if (type is LoadType.Data) {
    if (hasData && type.data.startPage > 0) { //мержим, если уже есть dataList + это не перезагрузка списка
        data.get().merge(type.data).asOptional()
    } else {
        type.data.asOptional()
    }
} else {
    data
}

fun <T> LoadableState<T>.observeMainLoading() = observeLoad
        .filter { it is MainLoading }
        .map { it.isLoading }

fun <T> LoadableState<T>.observeTransparentLoading() = observeLoad
        .filter { it is TransparentLoading }
        .map { it.isLoading }

fun <T> LoadableState<T>.observeSwrLoading() = observeLoad
        .filter { it is SwipeRefreshLoading }
        .map { it.isLoading }