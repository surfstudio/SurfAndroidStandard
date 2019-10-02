package ru.surfstudio.android.core.mvi.ui.middleware.dsl.transformers

import io.reactivex.Observable

class ReactTransformer<T, E>(
        private val mapper: (T) -> Unit
) : StreamTransformer<T, E> {

    override fun map(stream: Observable<T>): Observable<out E> {
        return stream.flatMap {
            mapper(it)
            Observable.empty<E>()
        }
    }
}