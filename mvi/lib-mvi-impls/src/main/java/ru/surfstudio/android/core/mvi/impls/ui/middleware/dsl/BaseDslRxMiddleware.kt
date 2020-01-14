package ru.surfstudio.android.core.mvi.impls.ui.middleware.dsl

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.ui.middleware.dsl.DslRxMiddleware

/**
 * [Middleware] with Rx and DSL support with    [EventTransformerList].
 */
interface BaseDslRxMiddleware<T : Event> : DslRxMiddleware<T, EventTransformerList<T>> {

    override fun provideTransformationList(eventStream: Observable<T>) = EventTransformerList(eventStream)
}