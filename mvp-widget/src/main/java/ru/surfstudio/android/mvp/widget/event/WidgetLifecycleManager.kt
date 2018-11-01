package ru.surfstudio.android.mvp.widget.event

import android.os.Bundle
import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager
import ru.surfstudio.android.core.ui.event.base.ScreenEvent
import ru.surfstudio.android.core.ui.event.base.ScreenEventDelegate
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
import ru.surfstudio.android.mvp.widget.event.delegate.WidgetScreenEventDelegateManager
import ru.surfstudio.android.mvp.widget.state.WidgetScreenState

/**
 * Управляет ЖЦ виджета
 * @param parentState состояние родителя
 * @param widgetScreenEventDelegateManager делегат менеджер виджета
 * @param screenState состояние виджета
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

    //разрешенные переходы по состояниям
    private val allowedStateTransition = mapOf(
            WidgetScreenState.States.CREATED to listOf(WidgetScreenState.States.VIEW_READY),
            WidgetScreenState.States.VIEW_READY to listOf(WidgetScreenState.States.STARTED),
            WidgetScreenState.States.STARTED to listOf(WidgetScreenState.States.RESUMED),
            WidgetScreenState.States.RESUMED to listOf(WidgetScreenState.States.PAUSED),
            WidgetScreenState.States.PAUSED to listOf(WidgetScreenState.States.RESUMED, WidgetScreenState.States.STOPPED),
            WidgetScreenState.States.STOPPED to listOf(WidgetScreenState.States.STARTED, WidgetScreenState.States.VIEW_DESTROYED),
            WidgetScreenState.States.VIEW_DESTROYED to listOf(WidgetScreenState.States.VIEW_READY, WidgetScreenState.States.DESTROYED)
    )

    //эвенты для состояний
    private val eventsMap = mapOf(
            WidgetScreenState.States.VIEW_READY to OnViewReadyEvent(),
            WidgetScreenState.States.STARTED to OnStartEvent(),
            WidgetScreenState.States.RESUMED to OnResumeEvent(),
            WidgetScreenState.States.PAUSED to OnPauseEvent(),
            WidgetScreenState.States.STOPPED to OnStopEvent(),
            WidgetScreenState.States.VIEW_DESTROYED to OnViewDestroyEvent(),
            WidgetScreenState.States.DESTROYED to OnCompletelyDestroyEvent()
    )

    init {
        parentScreenEventDelegateManager.registerDelegate(this)
    }

    override fun onSaveState(outState: Bundle?) {
        //stub
    }

    override fun onRestoreState(savedInstanceState: Bundle?) {
        //stub
    }

    fun onViewReady() {
        if (parentState.isCompletelyDestroyed) return

        if (pushState(WidgetScreenState.States.VIEW_READY)) {
            screenState.onViewReady()
        }
    }

    override fun onStart() {
        if (pushState(WidgetScreenState.States.STARTED)) {
            screenState.onStart()
        }
    }

    override fun onResume() {
        if (pushState(WidgetScreenState.States.RESUMED)) {
            screenState.onResume()
        }
    }

    override fun onPause() {
        if (parentState.isCompletelyDestroyed) return

        if (pushState(WidgetScreenState.States.PAUSED)) {
            screenState.onPause()
        }
    }

    override fun onStop() {
        if (screenState.isCompletelyDestroyed) return

        if (pushState(WidgetScreenState.States.STOPPED)) {
            screenState.onStop()
        }
    }

    override fun onViewDestroy() {
        if (screenState.isCompletelyDestroyed) return

        if (pushState(WidgetScreenState.States.VIEW_DESTROYED)) {
            screenState.onViewDestroy()
        }
    }

    override fun onCompletelyDestroy() {
        when (screenState.currentState) {
            WidgetScreenState.States.CREATED, WidgetScreenState.States.VIEW_READY -> {
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

    private fun pushState(state: WidgetScreenState.States): Boolean {
        if (allowedStateTransition[screenState.currentState]?.contains(state) ?: false) {
            widgetScreenEventDelegateManager.sendEvent<ScreenEvent, ScreenEventDelegate, Unit>(eventsMap[state]!!)
            return true
        }

        return false
    }

    private fun destroy() {
        widgetScreenEventDelegateManager.destroy()
    }
}