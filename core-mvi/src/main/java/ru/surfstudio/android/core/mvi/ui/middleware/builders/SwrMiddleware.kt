package ru.surfstudio.android.core.mvi.ui.middleware.builders

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.event.BaseSwipeRefreshEvent
import ru.surfstudio.android.core.mvi.ui.dsl.EventTransformerList
import ru.surfstudio.android.core.mvi.ui.middleware.RxMiddleware
import ru.surfstudio.android.core.mvi.util.filterIsInstance

/**
 * Middleware с поддержкой механизма обновления данных посредством SwipeRefresh
 */
interface SwrMiddleware<T : Event> : RxMiddleware<T> {

    fun Observable<T>.mapSwr() = filterIsInstance<BaseSwipeRefreshEvent>()
            .flatMap { loadDataSwr() }

    fun EventTransformerList<T>.mapSwr() = eventStream.mapSwr()

    fun loadDataSwr(): Observable<out T>
}