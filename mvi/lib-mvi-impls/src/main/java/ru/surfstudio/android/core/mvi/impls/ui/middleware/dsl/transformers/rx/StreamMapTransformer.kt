package ru.surfstudio.android.core.mvi.impls.ui.middleware.dsl.transformers.rx

import io.reactivex.Observable
import io.reactivex.ObservableTransformer

/**
 * Transformer which is responsible of mapping stream:
 * It takes stream of [Observable]<[T]> and transforms it to [Observable]<[E]>
 */
class StreamMapTransformer<T, E>(
        private val mapper: (Observable<T>) -> Observable<out E>
) : ObservableTransformer<T, E> {

    override fun apply(upstream: Observable<T>): Observable<E> {
        return mapper(upstream) as Observable<E>
    }
}
