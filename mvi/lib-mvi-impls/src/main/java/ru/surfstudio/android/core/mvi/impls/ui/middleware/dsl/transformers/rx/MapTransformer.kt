package ru.surfstudio.android.core.mvi.impls.ui.middleware.dsl.transformers.rx

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer

/**
 * Трансформер, выполняющий преобразование, аналогичное transform:
 * он берет элемент [T] и преобразовывает его в [E]
 */
class MapTransformer<T, E>(
        private val mapper: (T) -> E
) : ObservableTransformer<T, E> {

    override fun apply(upstream: Observable<T>): ObservableSource<E> {
        return upstream.map(mapper)
    }
}
