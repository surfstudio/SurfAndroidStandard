package ru.surfstudio.android.core.mvi.ui.middleware.dsl.transformers

import io.reactivex.Observable

class StreamMapTransformer<T, E>(
        private val mapper: (Observable<T>) -> Observable<out E>
) : StreamTransformer<T, E> {

    override fun map(stream: Observable<T>): Observable<out E> {
        return mapper(stream)
    }
}
