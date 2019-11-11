package ru.surfstudio.android.core.mvi.impls.ui.middleware.dsl.transformers.rx

import io.reactivex.Observable
import io.reactivex.ObservableTransformer

/**
 * Трансформер, выполняющий преобразование, аналогичное flatMap:
 * он берет элемент [T] и преобразовывает его в Observable<[E]>
 */
class EventMapTransformer<T, E>(
        private val mapper: (T) -> Observable<out E>
) : ObservableTransformer<T, E> {

    override fun apply(upstream: Observable<T>): Observable<E> {
        return upstream.flatMap(mapper)
    }
}
