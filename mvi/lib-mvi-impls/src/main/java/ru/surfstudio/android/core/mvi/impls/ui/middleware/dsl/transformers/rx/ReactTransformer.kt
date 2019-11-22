package ru.surfstudio.android.core.mvi.impls.ui.middleware.dsl.transformers.rx

import io.reactivex.Observable
import io.reactivex.ObservableTransformer

/**
 * Transformer which doesn't apply any direct transformations:
 * It simply reacts on each emission of [T] with callback.
 */
class ReactTransformer<T, E>(
        private val mapper: (T) -> Unit
) : ObservableTransformer<T, E> {

    override fun apply(upstream: Observable<T>): Observable<E> {
        return upstream.flatMap {
            mapper(it)
            Observable.empty<E>()
        }
    }
}