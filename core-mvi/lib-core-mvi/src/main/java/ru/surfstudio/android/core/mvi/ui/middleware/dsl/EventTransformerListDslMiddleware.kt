package ru.surfstudio.android.core.mvi.ui.middleware.dsl

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.event.Event

/**
 * [Middleware] с реализацией в Rx и поддержкой DSL с помощью [EventTransformerList].
 */
interface EventTransformerListDslMiddleware<T : Event> : DslMiddleware<T, EventTransformerList<T>> {

    override fun provideTransformationList(eventStream: Observable<T>) = EventTransformerList(eventStream)
}