package ru.surfstudio.android.core.mvi.optional

import io.reactivex.Observable

/**
 * Опционал, содержащий либо данные, либо ничего.
 * Используется для избежания проблем с nullable-переменными в Rx.
 */
sealed class Optional<out T> {

    val hasValue: Boolean
        get() = this !is Empty

    fun getOrNull(): T? = if (this is Some) value else null

    fun get() = (this as Some).value

    data class Some<out T>(val value: T) : Optional<T>()
    object Empty : Optional<Nothing>()
}

fun <T> T.asOptional() = Optional.Some(this)

fun <T> Observable<Optional<T>>.filterValue(): Observable<T> = this
        .filter { it is Optional.Some }
        .map { (it as Optional.Some).value }