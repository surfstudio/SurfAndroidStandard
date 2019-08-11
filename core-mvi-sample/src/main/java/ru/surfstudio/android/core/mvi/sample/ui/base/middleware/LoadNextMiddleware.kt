package ru.surfstudio.android.core.mvi.sample.ui.base.middleware

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.event.BaseLoadNextPageEvent
import ru.surfstudio.android.core.mvi.sample.ui.screen.list.EventTransformerList
import ru.surfstudio.android.core.mvi.util.filterIsInstance

/**
 * Middleware с поддержкой механизма подгрузки следующей порции данных
 */
interface LoadNextMiddleware<T : Event> : LoadableMiddleware<T> {

    fun Observable<T>.mapLoadNext(nextPageCallback: () -> Int) = filterIsInstance<BaseLoadNextPageEvent>()
            .flatMap { loadData(nextPageCallback()) }

    fun EventTransformerList<T>.mapLoadNext(nextPageCallback: () -> Int) = eventStream.mapLoadNext(nextPageCallback)
}