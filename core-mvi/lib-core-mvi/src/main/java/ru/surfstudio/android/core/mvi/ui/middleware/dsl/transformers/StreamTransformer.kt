package ru.surfstudio.android.core.mvi.ui.middleware.dsl.transformers

import io.reactivex.Observable

/**
 * Базовый трансформер потока событий RxMiddleware.
 * Преобразует входящий поток в исходящий, и используется в middleware
 */
interface StreamTransformer<T, R> {

    fun transform(stream: Observable<T>): Observable<out R>
}