package ru.surfstudio.android.core.mvp.binding.react.optional

import io.reactivex.Observable

sealed class Optional<out T> {
    data class Some<out T>(val value: T) : Optional<T>()
    class Empty : Optional<Nothing>()
}

val <T> Optional<T>.isEmpty: Boolean
    get() = this is Optional.Empty

val <T> Optional<T>.valueOrNull: T?
    get() = if (this is Optional.Some) value else null

fun <T> Observable<Optional<T>>.filterValue(): Observable<T> = this
        .filter { it is Optional.Some }
        .map { (it as Optional.Some).value }