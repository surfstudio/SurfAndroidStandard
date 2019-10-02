package ru.surfstudio.android.core.mvi.ui.middleware.dsl

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.ui.middleware.RxMiddleware

/**
 * [Middleware] с реализацией в Rx и поддержкой DSL.
 */
interface DslMiddleware<T : Event, L : List<Observable<T>>> : RxMiddleware<T> {

    fun provideTransformationList(eventStream: Observable<T>): L

    /**
     * Трансформация потока событий с помощью DSL
     */
    fun transformations(
            eventStream: Observable<T>,
            eventStreamBuilder: L.() -> Unit
    ): Observable<out T> {
        val streamTransformers = provideTransformationList(eventStream)
        eventStreamBuilder.invoke(streamTransformers)
        return merge(*streamTransformers.toTypedArray())
    }
}