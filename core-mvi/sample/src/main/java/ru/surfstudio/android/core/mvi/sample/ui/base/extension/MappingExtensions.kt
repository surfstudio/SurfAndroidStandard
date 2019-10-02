package ru.surfstudio.android.core.mvi.sample.ui.base.extension

import ru.surfstudio.android.core.mvp.binding.rx.extensions.Optional
import ru.surfstudio.android.core.mvp.binding.rx.extensions.toOptional
import ru.surfstudio.android.core.mvp.binding.rx.request.data.*
import ru.surfstudio.android.core.mvp.binding.rx.request.type.Request
import ru.surfstudio.android.datalistpagecount.domain.datalist.DataList

fun <T> mapLoading(type: Request<T>, hasData: Boolean, isSwr: Boolean = false): Loading {
    val isLoading = type is Request.Loading
    return when {
        isSwr ->
            SwipeRefreshLoading(isLoading)
        hasData ->
            TransparentLoading(isLoading)
        else ->
            MainLoading(isLoading)
    }
}

fun <T> mapError(type: Request<T>, hasData: Boolean): Throwable =
        if (!hasData && type is Request.Error) {
            type.error
        } else {
            EmptyErrorException()
        }

fun <T> mapData(type: Request<T>, data: Optional<T>): Optional<T> =
        if (type is Request.Success) {
            type.data.toOptional()
        } else {
            data
        }

fun <T> mapDataList(
        type: Request<DataList<T>>,
        data: Optional<DataList<T>>,
        hasData: Boolean = data.hasValue
): Optional<DataList<T>> = if (type is Request.Success) {
    if (hasData && type.data.startPage > 1) { //мержим, если уже есть dataList + это не перезагрузка списка
        data.get().merge(type.data).toOptional()
    } else {
        type.data.toOptional()
    }
} else {
    data
}