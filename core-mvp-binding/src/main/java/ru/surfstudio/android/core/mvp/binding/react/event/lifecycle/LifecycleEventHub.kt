package ru.surfstudio.android.core.mvp.binding.react.event.lifecycle

import android.os.Bundle
import ru.surfstudio.android.core.mvp.binding.react.event.Event
import ru.surfstudio.android.core.mvp.binding.react.event.hub.EventHub
import ru.surfstudio.android.core.ui.state.ScreenState

typealias LifecycleEventCreator<T> = (LifecycleStage) -> T

/**
 * [EventHub], рассылюащий события жизненного цикла экрана, к которому прикреплен
 */
interface LifecycleEventHub<T : Event> : EventHub<T>, LifecycleViewDelegate {

    val lifecycleEventCreator: LifecycleEventCreator<T>?

    val screenState: ScreenState

    override fun onViewReady() {
        if (!screenState.isViewRecreated) {
            emitEventByType(LifecycleStage.CREATE)
        }
        emitEventByType(LifecycleStage.VIEW_CREATE)
    }

    override fun onStart() {
        emitEventByType(LifecycleStage.START)
    }

    override fun onResume() {
        emitEventByType(LifecycleStage.RESUME)
    }

    override fun onPause() {
        emitEventByType(LifecycleStage.PAUSE)
    }

    override fun onStop() {
        emitEventByType(LifecycleStage.STOP)
    }

    override fun onViewDestroy() {
        emitEventByType(LifecycleStage.VIEW_DESTROY)
    }

    override fun onCompletelyDestroy() {
        emitEventByType(LifecycleStage.DESTROY)
    }

    fun emitEventByType(lifecycleStage: LifecycleStage) {
        when (val event = lifecycleEventCreator?.invoke(lifecycleStage)) {
            null -> return
            else -> emitEvent(event)
        }
    }

    override fun onSaveState(outState: Bundle?) {
        //TODO реакция на saveState (в базовом презентере реакции нет)
    }

    override fun onRestoreState(savedInstanceState: Bundle?) {
        //TODO реакция на saveState (в базовом презентере реакции нет)
    }
}