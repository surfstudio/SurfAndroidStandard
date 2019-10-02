package ru.surfstudio.android.core.mvi.ui.middleware.dsl.transformers

import io.reactivex.Observable

/**
 * Трансформер, выполняющий преобразование, аналогичное flatMap:
 * он берет элемент [T] и преобразовывает его в Observable<[E]>
 */
class EventMapTransformer<T, E>(
        private val mapper: (T) -> Observable<out E>
) : StreamTransformer<T, E> {

    override fun transform(stream: Observable<T>): Observable<out E> {
        return stream.flatMap { mapper(it) }
    }
}
