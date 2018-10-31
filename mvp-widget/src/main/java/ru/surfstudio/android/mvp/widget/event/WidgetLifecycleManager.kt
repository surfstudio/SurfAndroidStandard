package ru.surfstudio.android.mvp.widget.event

import android.os.Bundle
import ru.surfstudio.android.core.ui.event.lifecycle.completely.destroy.OnCompletelyDestroyDelegate
import ru.surfstudio.android.core.ui.event.lifecycle.completely.destroy.OnCompletelyDestroyEvent
import ru.surfstudio.android.core.ui.event.lifecycle.destroy.OnDestroyDelegate
import ru.surfstudio.android.core.ui.event.lifecycle.destroy.OnDestroyEvent
import ru.surfstudio.android.core.ui.event.lifecycle.pause.OnPauseDelegate
import ru.surfstudio.android.core.ui.event.lifecycle.pause.OnPauseEvent
import ru.surfstudio.android.core.ui.event.lifecycle.ready.OnViewReadyDelegate
import ru.surfstudio.android.core.ui.event.lifecycle.ready.OnViewReadyEvent
import ru.surfstudio.android.core.ui.event.lifecycle.resume.OnResumeDelegate
import ru.surfstudio.android.core.ui.event.lifecycle.resume.OnResumeEvent
import ru.surfstudio.android.core.ui.event.lifecycle.start.OnStartDelegate
import ru.surfstudio.android.core.ui.event.lifecycle.start.OnStartEvent
import ru.surfstudio.android.core.ui.event.lifecycle.state.OnRestoreStateDelegate
import ru.surfstudio.android.core.ui.event.lifecycle.state.OnSaveStateDelegate
import ru.surfstudio.android.core.ui.event.lifecycle.stop.OnStopDelegate
import ru.surfstudio.android.core.ui.event.lifecycle.stop.OnStopEvent
import ru.surfstudio.android.core.ui.event.lifecycle.view.destroy.OnViewDestroyDelegate
import ru.surfstudio.android.core.ui.event.lifecycle.view.destroy.OnViewDestroyEvent
import ru.surfstudio.android.core.ui.state.ScreenState
import ru.surfstudio.android.mvp.widget.event.delegate.WidgetScreenEventDelegateManager
import ru.surfstudio.android.mvp.widget.state.WidgetScreenState

/**
 * Управляет ЖЦ виджета
 * @param parentState состояние родителя
 * @param widgetScreenEventDelegateManager делегат менеджер виджета
 *
 * Todo подумать над названием
 */
class WidgetLifecycleManager(
        private val parentState: ScreenState,
        private val screenState: WidgetScreenState,
        private val widgetScreenEventDelegateManager: WidgetScreenEventDelegateManager
) : OnCompletelyDestroyDelegate,
        OnSaveStateDelegate,
        OnRestoreStateDelegate {

    override fun onSaveState(outState: Bundle?) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onRestoreState(savedInstanceState: Bundle?) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCompletelyDestroy() {
        when (screenState.currentState) {
            WidgetScreenState.States.PAUSED -> {
                onStop()
                onViewDetach()
                onDestroy()
            }
            WidgetScreenState.States.RESUMED -> {
                onPause()
                onStop()
                onViewDetach()
                onDestroy()
            }
        }

        widgetScreenEventDelegateManager.sendEvent<OnCompletelyDestroyEvent, OnCompletelyDestroyDelegate, Unit>(OnCompletelyDestroyEvent())
    }

    fun onCreate() {
        //асинхронность
        if (parentState.isCompletelyDestroyed) return
        widgetScreenEventDelegateManager.sendEvent<OnViewReadyEvent, OnViewReadyDelegate, Unit>(OnViewReadyEvent())
    }

    fun onStart() {
        if (parentState.isCompletelyDestroyed) return
        widgetScreenEventDelegateManager.sendEvent<OnStartEvent, OnStartDelegate, Unit>(OnStartEvent())
    }

    fun onResume() {
        if (parentState.isCompletelyDestroyed) return
        widgetScreenEventDelegateManager.sendEvent<OnResumeEvent, OnResumeDelegate, Unit>(OnResumeEvent())
    }

    fun onPause() {
        if (parentState.isCompletelyDestroyed) return
        widgetScreenEventDelegateManager.sendEvent<OnPauseEvent, OnPauseDelegate, Unit>(OnPauseEvent())
    }

    fun onStop() {
        if (parentState.isCompletelyDestroyed) return
        widgetScreenEventDelegateManager.sendEvent<OnStopEvent, OnStopDelegate, Unit>(OnStopEvent())
    }

    fun onViewDetach() {
        if (parentState.isCompletelyDestroyed) return
        widgetScreenEventDelegateManager.sendEvent<OnViewDestroyEvent, OnViewDestroyDelegate, Unit>(OnViewDestroyEvent())
    }

    fun onDestroy() {
        if (parentState.isCompletelyDestroyed) return
        widgetScreenEventDelegateManager.sendEvent<OnDestroyEvent, OnDestroyDelegate, Unit>(OnDestroyEvent())
    }
}