package ru.surfstudio.android.core.mvi.ui.middleware.dsl.transformers

import io.reactivex.Observable

/**
 * Трансформер, выполняющий преобразование, аналогичное transform:
 * он берет элемент [T] и преобразовывает его в [E]
 */
class MapTransformer<T, E>(
        private val mapper: (T) -> E
) : StreamTransformer<T, E> {

    override fun transform(stream: Observable<T>): Observable<out E> {
        return stream.map { mapper(it) }
    }
}
