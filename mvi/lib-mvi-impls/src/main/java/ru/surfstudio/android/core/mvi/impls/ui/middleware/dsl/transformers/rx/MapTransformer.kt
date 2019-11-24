package ru.surfstudio.android.core.mvi.impls.ui.middleware.dsl.transformers.rx

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer

/**
 * Transforms events like RxJava's map:
 * Takes element [T] and maps it to [E]
 */
class MapTransformer<T, E>(
        private val mapper: (T) -> E
) : ObservableTransformer<T, E> {

    override fun apply(upstream: Observable<T>): ObservableSource<E> {
        return upstream.map(mapper)
    }
}
