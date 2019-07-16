package ru.surfstudio.android.core.mvi.event

import ru.surfstudio.android.core.mvp.binding.rx.loadable.data.EmptyErrorException
import ru.surfstudio.android.core.mvp.binding.rx.loadable.data.LoadableData
import ru.surfstudio.android.core.mvp.binding.rx.loadable.data.MainLoading
import ru.surfstudio.android.core.mvp.binding.rx.loadable.type.LoadType
import ru.surfstudio.android.core.mvp.binding.rx.extensions.Optional
import ru.surfstudio.android.core.mvp.binding.rx.extensions.asOptional

/**
 * Событие загрузки данных
 *
 * @property type тип загрузки данных (Loading, Data, Error)
 */
interface LoadableEvent<T> : Event {
    var type: LoadType<T>

    val hasData get() = type is LoadType.Data

    fun toLoadableData(): LoadableData<T> {
        return when (type) {
            is LoadType.Data -> {
                val data = (type as LoadType.Data<T>).data
                LoadableData(data.asOptional(), MainLoading(false), EmptyErrorException())
            }

            is LoadType.Loading -> {
                LoadableData(Optional.empty(), MainLoading(true), EmptyErrorException())
            }

            is LoadType.Error -> {
                LoadableData(Optional.empty(), MainLoading(false), (type as LoadType.Error<T>).error)
            }
        }
    }
}