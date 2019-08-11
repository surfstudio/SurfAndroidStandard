package ru.surfstudio.android.core.mvi.sample.ui.base.middleware

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.event.lifecycle.LifecycleEvent
import ru.surfstudio.android.core.mvi.sample.ui.screen.list.EventTransformerList
import ru.surfstudio.android.core.mvi.ui.middleware.RxMiddleware
import ru.surfstudio.android.core.ui.state.LifecycleStage

/**
 * Middleware, реагирующий на события жизненного цикла
 */
interface LifecycleMiddleware<T : Event> : RxMiddleware<T> {
    fun Observable<T>.onCreate() = filterLifecycleEvent(LifecycleStage.CREATED)
    //TODO остальные этапы ЖЦ
    val EventTransformerList<T>.onCreate get() = eventStream.onCreate()
}

private fun <T> Observable<T>.filterLifecycleEvent(filterStage: LifecycleStage) = this
        .filter { it is LifecycleEvent && it.stage == filterStage }
        .map { it }