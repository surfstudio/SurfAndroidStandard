package ru.surfstudio.android.core.mvp.binding.react

import ru.surfstudio.android.core.mvp.binding.react.optional.Optional


abstract class NetworkRequestEvent<T> : Event {
    var data: Optional<T> = Optional.Empty()
    var isLoading: Boolean = false
    var error: Optional<Throwable> = Optional.Empty()

    fun acceptLoading() = apply { isLoading = true }
    fun acceptData(value: T) = apply { isLoading = false; data = Optional.Some(value) }
    fun acceptError(throwable: Throwable) = apply { isLoading = false; error = Optional.Some(throwable) }
}