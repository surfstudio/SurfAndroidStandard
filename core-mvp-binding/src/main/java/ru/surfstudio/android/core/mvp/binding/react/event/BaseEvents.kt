package ru.surfstudio.android.core.mvp.binding.react.event

import ru.surfstudio.android.core.mvp.binding.react.optional.Optional


open class RefreshData : Event

open class LoadData : Event {
    open class First : LoadData()
    open class Next : LoadData()
    open class All : LoadData()
}

abstract class RxEvent<T> : Event {

    var data: Optional<T> = Optional.Empty
        protected set
    var isLoading: Boolean = false
        protected set
    var error: Optional<Throwable> = Optional.Empty
        protected set
    var type: RequestType = RequestType.Loading
        protected set

    fun acceptLoading() = apply {
        type = RequestType.Loading
        isLoading = true
    }

    fun acceptData(value: T) = apply {
        type = RequestType.Data
        isLoading = false
        data = Optional.Some(value)
    }

    fun acceptError(throwable: Throwable) = apply {
        type = RequestType.Error
        isLoading = false
        error = Optional.Some(throwable)
    }
}

enum class RequestType {
    Loading, Data, Error
}