package ru.surfstudio.android.core.mvp.binding.react.event.lifecycle

import android.os.Bundle
import ru.surfstudio.android.core.mvp.binding.react.event.hub.EventHub
import ru.surfstudio.android.core.ui.state.ScreenState

interface LifecycleEventHub : EventHub, LifecycleViewDelegate {

    val screenState: ScreenState

    override fun onViewReady() {
        if (!screenState.isViewRecreated) {
            emitEvent(LifecycleEvent.CREATE)
        }
        emitEvent(LifecycleEvent.VIEW_CREATE)
    }

    override fun onStart() {
        emitEvent(LifecycleEvent.START)
    }

    override fun onResume() {
        emitEvent(LifecycleEvent.RESUME)
    }

    override fun onPause() {
        emitEvent(LifecycleEvent.PAUSE)
    }

    override fun onStop() {
        emitEvent(LifecycleEvent.STOP)
    }

    override fun onViewDestroy() {
        emitEvent(LifecycleEvent.VIEW_DESTROY)
    }

    override fun onCompletelyDestroy() {
        emitEvent(LifecycleEvent.DESTROY)
    }

    override fun onSaveState(outState: Bundle?) {
        //TODO реакция на saveState (в базовом презентере реакции нет)
    }

    override fun onRestoreState(savedInstanceState: Bundle?) {
        //TODO реакция на saveState (в базовом презентере реакции нет)
    }
}