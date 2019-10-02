package ru.surfstudio.android.core.mvi.ui.middleware.dsl.transformers

import io.reactivex.Observable

class MapTransformer<T, E>(
        private val mapper: (T) -> E
) : StreamTransformer<T, E> {

    override fun map(stream: Observable<T>): Observable<out E> {
        return stream.map { mapper(it) }
    }
}
