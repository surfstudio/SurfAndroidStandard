package ru.surfstudio.android.core.mvi.impls.ui.middleware.dsl.transformers.rx

import io.reactivex.Observable
import io.reactivex.ObservableTransformer

/**
 * Трансформер, не выполняющий преобразований:
 * он просто реагирует на каждый эмит эвента действием, результат которого игнорируется.
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