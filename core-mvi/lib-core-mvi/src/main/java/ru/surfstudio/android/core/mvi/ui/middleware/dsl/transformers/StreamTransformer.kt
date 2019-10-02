package ru.surfstudio.android.core.mvi.ui.middleware.dsl.transformers

import io.reactivex.Observable

/**
 * Базовый трансформер потока событий RxMiddleware
 */
interface StreamTransformer<T, R> {

    fun map(stream: Observable<T>): Observable<out R>
}