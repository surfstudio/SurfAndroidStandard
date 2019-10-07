package ru.surfstudio.android.core.mvi.ui.middleware.dsl.transformers.rx

import io.reactivex.Observable

/**
 * Трансформер, выполняющий преобразование целого потока:
 * он берет поток Observable<[T]> и преобразовывает его в другой поток Observable<[E]>
 */
class StreamMapTransformer<T, E>(
        private val mapper: (Observable<T>) -> Observable<out E>
) : StreamTransformer<T, E> {

    override fun transform(stream: Observable<T>): Observable<out E> {
        return mapper(stream)
    }
}
