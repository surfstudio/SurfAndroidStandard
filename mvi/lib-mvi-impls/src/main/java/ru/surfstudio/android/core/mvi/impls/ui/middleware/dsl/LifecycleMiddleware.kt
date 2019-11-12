package ru.surfstudio.android.core.mvi.impls.ui.middleware.dsl

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.event.lifecycle.LifecycleEvent
import ru.surfstudio.android.core.mvi.event.lifecycle.LifecycleEventHub
import ru.surfstudio.android.core.mvi.ui.middleware.RxMiddleware
import ru.surfstudio.android.core.ui.state.LifecycleStage

/**
 * Middleware, that reacts on android lifecycle.
 *
 * To receive events, you need EventHub to implement interface [LifecycleEventHub]
 */
interface LifecycleMiddleware<T : Event> : RxMiddleware<T> {
    fun Observable<T>.onCreate() = filterLifecycleEvent(LifecycleStage.CREATED)
    fun Observable<T>.onViewRecreate() = filterLifecycleEvent(LifecycleStage.VIEW_CREATED)
    fun Observable<T>.onStart() = filterLifecycleEvent(LifecycleStage.STARTED)
    fun Observable<T>.onResume() = filterLifecycleEvent(LifecycleStage.RESUMED)
    fun Observable<T>.onPause() = filterLifecycleEvent(LifecycleStage.PAUSED)
    fun Observable<T>.onStop() = filterLifecycleEvent(LifecycleStage.STOPPED)
    fun Observable<T>.onViewDestroy() = filterLifecycleEvent(LifecycleStage.VIEW_DESTROYED)
    fun Observable<T>.onDestroy() = filterLifecycleEvent(LifecycleStage.COMPLETELY_DESTROYED)

    fun EventTransformerList<T>.onCreate() = eventStream.onCreate()
    fun EventTransformerList<T>.onViewRecreate() = eventStream.onViewRecreate()
    fun EventTransformerList<T>.onStart() = eventStream.onStart()
    fun EventTransformerList<T>.onResume() = eventStream.onResume()
    fun EventTransformerList<T>.onPause() = eventStream.onPause()
    fun EventTransformerList<T>.onStop() = eventStream.onStop()
    fun EventTransformerList<T>.onViewDestroy() = eventStream.onViewDestroy()
    fun EventTransformerList<T>.onDestroy() = eventStream.onDestroy()
}

private fun <T> Observable<T>.filterLifecycleEvent(filterStage: LifecycleStage) = this
        .filter { it is LifecycleEvent && it.stage == filterStage }
        .map { it }