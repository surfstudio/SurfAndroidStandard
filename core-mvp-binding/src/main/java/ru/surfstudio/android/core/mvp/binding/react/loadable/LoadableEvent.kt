package ru.surfstudio.android.core.mvp.binding.react.loadable

import ru.surfstudio.android.core.mvp.binding.react.loadable.data.EmptyErrorException
import ru.surfstudio.android.core.mvp.binding.react.loadable.data.LoadableData
import ru.surfstudio.android.core.mvp.binding.react.loadable.data.MainLoading
import ru.surfstudio.android.core.mvp.binding.react.optional.Optional
import ru.surfstudio.android.core.mvp.binding.react.optional.asOptional

sealed class LoadType<T> {
    data class Data<T>(val data: T) : LoadType<T>()
    data class Loading<T>(val isLoading: Boolean = true) : LoadType<T>()
    data class Error<T>(val error: Throwable = EmptyErrorException()) : LoadType<T>()
}

interface LoadableEvent<T> {
    var type: LoadType<T>

    fun toLoadableData(): LoadableData<T> {
        return when (type) {
            is LoadType.Data -> {
                val data = (type as LoadType.Data<T>).data
                LoadableData(data.asOptional(), MainLoading(false), EmptyErrorException())
            }

            is LoadType.Loading -> {
                LoadableData(Optional.Empty, MainLoading(true), EmptyErrorException())
            }

            is LoadType.Error -> {
                LoadableData(Optional.Empty, MainLoading(false), (type as LoadType.Error<T>).error)
            }
        }
    }
}