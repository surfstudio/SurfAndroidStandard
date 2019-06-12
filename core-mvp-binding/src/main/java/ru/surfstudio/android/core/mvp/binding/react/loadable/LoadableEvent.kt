package ru.surfstudio.android.core.mvp.binding.react.loadable

import ru.surfstudio.android.core.mvp.binding.react.event.Event
import ru.surfstudio.android.core.mvp.binding.react.optional.Optional

abstract class LoadableEvent<T> : Event {

    var value = LoadableData<T>()
    var type = LoadableType.Loading

    val data get() = value.data
    val isLoading get() = value.isLoading
    val error get() = value.error

    fun acceptLoading() = apply {
        type = LoadableType.Loading
        value = value.copy(
                isLoading = true
        )
    }

    fun acceptData(data: T) = apply {
        type = LoadableType.Data
        value = value.copy(
                isLoading = false,
                data = Optional.Some(data)
        )
    }

    fun acceptError(throwable: Throwable) = apply {
        type = LoadableType.Error
        value = value.copy(
                isLoading = false,
                error = Optional.Some(throwable)
        )
    }

    override fun toString() = "${super.toString()}, stage:$type"
}