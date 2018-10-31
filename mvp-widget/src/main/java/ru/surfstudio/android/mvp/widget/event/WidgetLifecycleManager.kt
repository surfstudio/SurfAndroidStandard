package ru.surfstudio.android.mvp.widget.event

import android.os.Bundle
import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager
import ru.surfstudio.android.core.ui.event.lifecycle.completely.destroy.OnCompletelyDestroyDelegate
import ru.surfstudio.android.core.ui.event.lifecycle.completely.destroy.OnCompletelyDestroyEvent
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
import ru.surfstudio.android.logger.Logger
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
        private val widgetScreenEventDelegateManager: WidgetScreenEventDelegateManager,
        parentScreenEventDelegateManager: ScreenEventDelegateManager
) : OnCompletelyDestroyDelegate,
        OnSaveStateDelegate,
        OnRestoreStateDelegate,
        OnStartDelegate,
        OnResumeDelegate,
        OnPauseDelegate,
        OnStopDelegate,
        OnViewDestroyDelegate {

    init {
        parentScreenEventDelegateManager.registerDelegate(this)
    }

    override fun onSaveState(outState: Bundle?) {
        //TODO("not implemented")
    }

    override fun onRestoreState(savedInstanceState: Bundle?) {
        //TODO("not implemented")
    }

    fun onViewReady() {
        //асинхронность
        Logger.d("11111 Widget sendEvent ViewReady")
        if (screenState.currentState == WidgetScreenState.States.VIEW_DESTROYED || screenState.currentState == WidgetScreenState.States.CREATED) {
            screenState.onViewReady()
            widgetScreenEventDelegateManager.sendEvent<OnViewReadyEvent, OnViewReadyDelegate, Unit>(OnViewReadyEvent())
        }
    }

    override fun onStart() {
        if (screenState.currentState == WidgetScreenState.States.VIEW_READY || screenState.currentState == WidgetScreenState.States.STOPPED) {
            Logger.d("11111 Widget sendEvent StartEvent")
            screenState.onStart()
            widgetScreenEventDelegateManager.sendEvent<OnStartEvent, OnStartDelegate, Unit>(OnStartEvent())
        }
    }

    override fun onResume() {
        if (screenState.currentState.ordinal in (WidgetScreenState.States.STARTED.ordinal..WidgetScreenState.States.RESUMED.ordinal + 1)) {
            Logger.d("11111 Widget sendEvent Resume")
            screenState.onResume()
            widgetScreenEventDelegateManager.sendEvent<OnResumeEvent, OnResumeDelegate, Unit>(OnResumeEvent())
        }
    }

    override fun onPause() {
        if (parentState.isCompletelyDestroyed) return
        if (screenState.currentState == WidgetScreenState.States.RESUMED) {
            Logger.d("11111 Widget sendEvent Pause")
            screenState.onPause()
            widgetScreenEventDelegateManager.sendEvent<OnPauseEvent, OnPauseDelegate, Unit>(OnPauseEvent())
        }
    }

    override fun onStop() {
        if (screenState.isCompletelyDestroyed) return
        if (screenState.currentState == WidgetScreenState.States.PAUSED) {
            Logger.d("11111 Widget sendEvent Stop")
            screenState.onStop()
            widgetScreenEventDelegateManager.sendEvent<OnStopEvent, OnStopDelegate, Unit>(OnStopEvent())
        }
    }

    override fun onViewDestroy() {
        if (screenState.isCompletelyDestroyed) return
        if (screenState.currentState == WidgetScreenState.States.STOPPED) {
            Logger.d("11111 Widget sendEvent ViewDestroy")
            screenState.onViewDestroy()
            widgetScreenEventDelegateManager.sendEvent<OnViewDestroyEvent, OnViewDestroyDelegate, Unit>(OnViewDestroyEvent())
        }
    }

    override fun onCompletelyDestroy() {
        Logger.d("1111 LifecycleManager onCompletelyDestroy| widgetState = ${screenState.currentState}")
        when (screenState.currentState) {
            WidgetScreenState.States.CREATED -> {
                onStart()
                onResume()
                onPause()
                onStop()
                onViewDestroy()
            }
            WidgetScreenState.States.STARTED -> {
                onResume()
                onPause()
                onStop()
                onViewDestroy()
            }
            WidgetScreenState.States.RESUMED -> {
                onPause()
                onStop()
                onViewDestroy()
            }
            WidgetScreenState.States.PAUSED -> {
                onStop()
                onViewDestroy()
            }
            WidgetScreenState.States.STOPPED -> onViewDestroy()
        }

        screenState.onDestroy()
        widgetScreenEventDelegateManager.sendEvent<OnCompletelyDestroyEvent, OnCompletelyDestroyDelegate, Unit>(OnCompletelyDestroyEvent())
        destroy()
    }

    fun destroy() {
        widgetScreenEventDelegateManager.destroy()
    }
}