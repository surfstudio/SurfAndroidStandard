package ru.surfstudio.android.core.mvi.sample.ui.base.extension

import ru.surfstudio.android.core.mvp.binding.rx.request.data.*
import ru.surfstudio.android.core.mvp.binding.rx.request.type.Request
import ru.surfstudio.android.datalistpagecount.domain.datalist.DataList

fun <T> mapLoading(request: Request<T>, hasData: Boolean, isSwr: Boolean = false): Loading {
    val isLoading = request is Request.Loading
    return when {
        isSwr ->
            SwipeRefreshLoading(isLoading)
        hasData ->
            TransparentLoading(isLoading)
        else ->
            MainLoading(isLoading)
    }
}

fun <T> mapError(request: Request<T>, hasData: Boolean): Throwable? =
        if (!hasData && request is Request.Error) {
            request.error
        } else {
            null
        }

fun <T> mapData(request: Request<T>, data: T?): T? =
        if (request is Request.Success) {
            request.data
        } else {
            data
        }

fun <T> mapDataList(
        request: Request<DataList<T>>,
        data: DataList<T>?,
        hasData: Boolean = data != null
): DataList<T>? = if (request is Request.Success) {
    if (hasData && request.data.startPage > 1) { //мержим, если уже есть dataList + это не перезагрузка списка
        data?.merge(request.data)
    } else {
        request.data
    }
} else {
    data
}