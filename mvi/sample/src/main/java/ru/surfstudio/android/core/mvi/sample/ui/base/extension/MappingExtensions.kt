package ru.surfstudio.android.core.mvi.sample.ui.base.extension

import ru.surfstudio.android.core.mvp.binding.rx.request.data.*
import ru.surfstudio.android.core.mvp.binding.rx.request.Request
import ru.surfstudio.android.datalistpagecount.domain.datalist.DataList

fun <T> mapLoading(request: Request<T>, hasData: Boolean, isSwr: Boolean = false): Loading {
    val isLoading = request.isLoading
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
        if (!hasData && request.isError) {
            request.extractError()
        } else {
            null
        }

fun <T> mapData(request: Request<T>, data: T?): T? =
        if (request.isSuccess) {
            request.extractData()
        } else {
            data
        }

fun <T> mapDataList(
        request: Request<DataList<T>>,
        data: DataList<T>?,
        hasData: Boolean = data != null
): DataList<T>? =
        when {
            request.isSuccess -> {
                when {
                    hasData && request.extractData().startPage > 1 -> data?.merge(request.extractData())
                    else -> request.extractData()
                }
            }
            else -> data
        }