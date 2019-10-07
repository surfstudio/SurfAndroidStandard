package ru.surfstudio.android.core.mvi.ui.middleware.dsl

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.ui.middleware.RxMiddleware

/**
 * [Middleware] с реализацией в Rx и поддержкой DSL.
 */
interface DslRxMiddleware<T : Event, L : List<Observable<T>>> : RxMiddleware<T>, DslMiddleware<T, Observable<T>, Observable<out T>, L> {

    override fun combineTransformations(transformations: List<Observable<out T>>): Observable<out T> =
            merge(*transformations.toTypedArray())
}