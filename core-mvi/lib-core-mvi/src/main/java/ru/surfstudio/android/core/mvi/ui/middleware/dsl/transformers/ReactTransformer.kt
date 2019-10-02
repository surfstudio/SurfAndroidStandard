package ru.surfstudio.android.core.mvi.ui.middleware.dsl.transformers

import io.reactivex.Observable

/**
 * Трансформер, не выполняющий преобразований:
 * он просто реагирует на каждый эмит эвента действием, результат которого игнорируется.
 */
class ReactTransformer<T, E>(
        private val mapper: (T) -> Unit
) : StreamTransformer<T, E> {

    override fun transform(stream: Observable<T>): Observable<out E> {
        return stream.flatMap {
            mapper(it)
            Observable.empty<E>()
        }
    }
}