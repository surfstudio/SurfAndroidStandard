package ru.surfstudio.android.core.mvi.event.lifecycle

import android.os.Bundle
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.event.factory.EventFactory
import ru.surfstudio.android.core.mvi.event.hub.EventHub
import ru.surfstudio.android.core.ui.state.LifecycleStage
import ru.surfstudio.android.core.ui.state.ScreenState

/**
 * [EventHub], рассылюащий события жизненного цикла экрана, к которому прикреплен
 */
interface LifecycleEventHub<T : Event, Stream> : EventHub<T, Stream>, LifecycleViewDelegate {

    val lifecycleEventCreator: EventFactory<LifecycleStage, T>?

    val screenState: ScreenState

    override fun onViewReady() {
        if (!screenState.isViewRecreated) {
            emitEventByType(LifecycleStage.CREATED)
        }
        emitEventByType(LifecycleStage.VIEW_CREATED)
    }

    override fun onStart() {
        emitEventByType(LifecycleStage.STARTED)
    }

    override fun onResume() {
        emitEventByType(LifecycleStage.RESUMED)
    }

    override fun onPause() {
        emitEventByType(LifecycleStage.PAUSED)
    }

    override fun onStop() {
        emitEventByType(LifecycleStage.STOPPED)
    }

    override fun onViewDestroy() {
        emitEventByType(LifecycleStage.VIEW_DESTROYED)
    }

    override fun onCompletelyDestroy() {
        emitEventByType(LifecycleStage.COMPLETELY_DESTROYED)
    }

    fun emitEventByType(lifecycleStage: LifecycleStage) {
        val event = lifecycleEventCreator?.invoke(lifecycleStage)
        if (event != null) {
            emit(event)
        }
    }

    override fun onSaveState(outState: Bundle?) {
        //TODO реакция на saveState (в базовом презентере реакции нет)
    }

    override fun onRestoreState(savedInstanceState: Bundle?) {
        //TODO реакция на saveState (в базовом презентере реакции нет)
    }
}