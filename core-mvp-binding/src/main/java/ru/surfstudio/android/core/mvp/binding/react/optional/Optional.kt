package ru.surfstudio.android.core.mvp.binding.react.optional

import io.reactivex.Observable

sealed class Optional<out T> {

    val isEmpty: Boolean
        get() = this is Empty

    val valueOrNull: T?
        get() = if (this is Some) value else null

    val forcedValue: T
        get() = (this as Some).value

    data class Some<out T>(val value: T) : Optional<T>()
    object Empty : Optional<Nothing>()
}


fun <T> Observable<Optional<T>>.filterValue(): Observable<T> = this
        .filter { it is Optional.Some }
        .map { (it as Optional.Some).value }