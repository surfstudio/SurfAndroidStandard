package ru.surfstudio.android.core.mvi.impls.ui.middleware.dsl.transformers.rx

import io.reactivex.Observable
import io.reactivex.ObservableTransformer

/**
 * Transforms events like RxJava's flatMap:
 * Takes element [T] and maps it to [Observable]<[E]>
 */
class EventMapTransformer<T, E>(
        private val mapper: (T) -> Observable<out E>
) : ObservableTransformer<T, E> {

    override fun apply(upstream: Observable<T>): Observable<E> {
        return upstream.flatMap(mapper)
    }
}
