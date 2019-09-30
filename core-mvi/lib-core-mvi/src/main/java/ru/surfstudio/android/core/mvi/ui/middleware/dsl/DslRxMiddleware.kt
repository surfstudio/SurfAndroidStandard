package ru.surfstudio.android.core.mvi.ui.middleware.dsl

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.ui.middleware.RxMiddleware
import ru.surfstudio.android.core.mvi.ui.relation.StateEmitter

/**
 * [Middleware] с реализацией в Rx и поддержкой DSL с помощью [EventTransformerList].
 */
interface DslRxMiddleware<T : Event> : RxMiddleware<T>, StateEmitter {

    /**
     * Трансформация потока событий с помощью DSL
     */
    fun transformations(
            eventStream: Observable<T>,
            eventStreamBuilder: EventTransformerList<T>.() -> Unit
    ): Observable<out T> {
        val streamTransformers = EventTransformerList(eventStream)
        eventStreamBuilder.invoke(streamTransformers)
        return merge(*streamTransformers.toTypedArray())
    }
}