package ru.surfstudio.android.core.mvi.impls.ui.middleware.dsl.transformers.rx

import io.reactivex.Observable
import io.reactivex.ObservableTransformer

/**
 * Трансформер, выполняющий преобразование целого потока:
 * он берет поток Observable<[T]> и преобразовывает его в другой поток Observable<[E]>
 */
class StreamMapTransformer<T, E>(
        private val mapper: (Observable<T>) -> Observable<out E>
) : ObservableTransformer<T, E> {

    override fun apply(upstream: Observable<T>): Observable<E> {
        return mapper(upstream) as Observable<E>
    }
}
