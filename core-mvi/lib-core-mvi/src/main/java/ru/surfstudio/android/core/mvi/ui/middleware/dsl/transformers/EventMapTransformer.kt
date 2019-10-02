package ru.surfstudio.android.core.mvi.ui.middleware.dsl.transformers

import io.reactivex.Observable

class EventMapTransformer<T, E>(
        private val mapper: (T) -> Observable<out E>
) : StreamTransformer<T, E> {

    override fun map(stream: Observable<T>): Observable<out E> {
        return stream.flatMap { mapper(it) }
    }
}
