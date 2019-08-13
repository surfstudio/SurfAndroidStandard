package ru.surfstudio.android.core.mvi.ui.middleware.builders

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.event.BaseLoadNextPageEvent
import ru.surfstudio.android.core.mvi.ui.dsl.EventTransformerList
import ru.surfstudio.android.core.mvi.ui.middleware.RxMiddleware
import ru.surfstudio.android.core.mvi.util.filterIsInstance

/**
 * Middleware с поддержкой механизма подгрузки следующей порции данных
 */
interface LoadNextPageMiddleware<T : Event> : RxMiddleware<T> {

    fun Observable<T>.mapLoadNext() = filterIsInstance<BaseLoadNextPageEvent>()
            .flatMap { loadNextData() }

    fun EventTransformerList<T>.mapLoadNext() =
            eventStream.mapLoadNext()

    fun loadNextData(): Observable<out T>
}