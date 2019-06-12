package ru.surfstudio.android.core.mvp.binding.react.loadable

import ru.surfstudio.android.core.mvp.binding.react.event.Event
import ru.surfstudio.android.core.mvp.binding.react.loadable.data.EmptyErrorException
import ru.surfstudio.android.core.mvp.binding.react.loadable.data.LoadableData
import ru.surfstudio.android.core.mvp.binding.react.optional.Optional

abstract class LoadableEvent<T> : Event {

    var data: Optional<T> = Optional.Empty
    var isLoading: Boolean = false
    var error: Throwable = EmptyErrorException()

    var type = LoadableType.Loading

    fun acceptLoading() = apply {
        type = LoadableType.Loading
        isLoading = true
    }

    fun acceptData(value: T) = apply {
        type = LoadableType.Data
        isLoading = false
        data = Optional.Some(value)
        error = EmptyErrorException()
    }

    fun acceptError(throwable: Throwable) = apply {
        type = LoadableType.Error
        isLoading = false
        error = throwable
    }

    override fun toString() = "${super.toString()}, stage:$type"
}