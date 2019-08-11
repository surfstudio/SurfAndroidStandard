package ru.surfstudio.android.core.mvi.sample.ui.base.middleware

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.event.BaseSwipeRefreshEvent
import ru.surfstudio.android.core.mvi.sample.ui.screen.list.EventTransformerList
import ru.surfstudio.android.core.mvi.util.filterIsInstance

/**
 * Middleware с поддержкой механизма обновления данных посредством SwipeRefresh
 */
interface SwrMiddleware<T : Event> : LoadableMiddleware<T> {

    fun Observable<T>.mapSwr() = filterIsInstance<BaseSwipeRefreshEvent>()
            .flatMap { loadData(isSwr = true) }

    fun EventTransformerList<T>.mapSwr() = eventStream.mapSwr()

}