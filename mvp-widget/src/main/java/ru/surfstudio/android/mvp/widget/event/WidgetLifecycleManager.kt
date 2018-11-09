package ru.surfstudio.android.mvp.widget.event

import android.view.View
import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager
import ru.surfstudio.android.core.ui.event.base.ScreenEvent
import ru.surfstudio.android.core.ui.event.base.ScreenEventDelegate
import ru.surfstudio.android.core.ui.event.lifecycle.completely.destroy.OnCompletelyDestroyDelegate
import ru.surfstudio.android.core.ui.event.lifecycle.completely.destroy.OnCompletelyDestroyEvent
import ru.surfstudio.android.core.ui.event.lifecycle.pause.OnPauseDelegate
import ru.surfstudio.android.core.ui.event.lifecycle.pause.OnPauseEvent
import ru.surfstudio.android.core.ui.event.lifecycle.ready.OnViewReadyEvent
import ru.surfstudio.android.core.ui.event.lifecycle.resume.OnResumeDelegate
import ru.surfstudio.android.core.ui.event.lifecycle.resume.OnResumeEvent
import ru.surfstudio.android.core.ui.event.lifecycle.start.OnStartDelegate
import ru.surfstudio.android.core.ui.event.lifecycle.start.OnStartEvent
import ru.surfstudio.android.core.ui.event.lifecycle.stop.OnStopDelegate
import ru.surfstudio.android.core.ui.event.lifecycle.stop.OnStopEvent
import ru.surfstudio.android.core.ui.event.lifecycle.view.destroy.OnViewDestroyDelegate
import ru.surfstudio.android.core.ui.event.lifecycle.view.destroy.OnViewDestroyEvent
import ru.surfstudio.android.core.ui.state.ScreenState
import ru.surfstudio.android.core.ui.state.ScreenStates
import ru.surfstudio.android.mvp.widget.delegate.WidgetViewDelegate
import ru.surfstudio.android.mvp.widget.event.delegate.WidgetScreenEventDelegateManager
import ru.surfstudio.android.mvp.widget.state.WidgetScreenState
import ru.surfstudio.android.mvp.widget.view.CoreWidgetViewInterface

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
        OnStartDelegate,
        OnResumeDelegate,
        OnPauseDelegate,
        OnStopDelegate,
        OnViewDestroyDelegate {

    private lateinit var widgetViewDelegate: WidgetViewDelegate

    //разрешенные переходы по состояниям
    private val allowedStateTransition = mapOf(
            ScreenStates.CREATED to listOf(ScreenStates.VIEW_READY, ScreenStates.DESTROYED),
            ScreenStates.VIEW_READY to listOf(ScreenStates.STARTED, ScreenStates.VIEW_DESTROYED),
            ScreenStates.STARTED to listOf(ScreenStates.RESUMED, ScreenStates.STOPPED),
            ScreenStates.RESUMED to listOf(ScreenStates.PAUSED),
            ScreenStates.PAUSED to listOf(ScreenStates.RESUMED, ScreenStates.STOPPED),
            ScreenStates.STOPPED to listOf(ScreenStates.STARTED, ScreenStates.VIEW_DESTROYED),
            ScreenStates.VIEW_DESTROYED to listOf(ScreenStates.VIEW_READY, ScreenStates.DESTROYED),
            ScreenStates.DESTROYED to listOf()
    )

    //разрешенные переходы по состояниям
    private val screenStateEvents = mapOf(
            ScreenStates.VIEW_READY to { screenState.onViewReady() },
            ScreenStates.STARTED to { screenState.onStart() },
            ScreenStates.RESUMED to { screenState.onResume() },
            ScreenStates.PAUSED to { screenState.onPause() },
            ScreenStates.STOPPED to { screenState.onStop() },
            ScreenStates.VIEW_DESTROYED to { screenState.onViewDestroy() },
            ScreenStates.DESTROYED to { screenState.onDestroy() }
    )

    //эвенты для состояний
    private val eventsMap = mapOf(
            ScreenStates.VIEW_READY to OnViewReadyEvent(),
            ScreenStates.STARTED to OnStartEvent(),
            ScreenStates.RESUMED to OnResumeEvent(),
            ScreenStates.PAUSED to OnPauseEvent(),
            ScreenStates.STOPPED to OnStopEvent(),
            ScreenStates.VIEW_DESTROYED to OnViewDestroyEvent(),
            ScreenStates.DESTROYED to OnCompletelyDestroyEvent()
    )

    private val parentsStatesEvents = mapOf(
            ScreenStates.RESUMED to { state: ScreenStates -> allowedStateTransition[state] }
    )

    init {
        parentScreenEventDelegateManager.registerDelegate(this)
    }

    fun onCreate(widgetView: View, coreWidgetView: CoreWidgetViewInterface, widgetViewDelegate: WidgetViewDelegate) {
        screenState.onCreate(widgetView, coreWidgetView)

        //возможные проблемы(так как держим ссылку на делегат, хоть и каждый раз обновляем)
        this.widgetViewDelegate = widgetViewDelegate
    }

    fun onViewReady() {
        pushState(ScreenStates.VIEW_READY)
    }

    override fun onStart() {
        pushState(ScreenStates.STARTED)
    }

    override fun onResume() {
        pushState(ScreenStates.RESUMED)
    }

    override fun onPause() {
        pushState(ScreenStates.PAUSED)
    }

    override fun onStop() {
        pushState(ScreenStates.STOPPED)
    }

    override fun onViewDestroy() {
        pushState(ScreenStates.VIEW_DESTROYED)
    }

    override fun onCompletelyDestroy() {
        pushState(ScreenStates.DESTROYED)
        destroy()

        //возможные проблемы
        widgetViewDelegate.onCompletelyDestroy()
    }

    private fun pushState(wishingState: ScreenStates) {
        when {

            wishingState == ScreenStates.STOPPED && parentState.currentState == ScreenStates.RESUMED -> {
                applyStates(ScreenStates.PAUSED, ScreenStates.STOPPED, ScreenStates.VIEW_DESTROYED)
            }

            wishingState == ScreenStates.STARTED && parentState.currentState == ScreenStates.RESUMED -> {
                applyStates(ScreenStates.STARTED, ScreenStates.RESUMED)
            }

            wishingState == ScreenStates.DESTROYED -> {
                when (screenState.currentState!!) {

                    ScreenStates.CREATED, ScreenStates.VIEW_READY, ScreenStates.STOPPED -> {
                        applyStates(ScreenStates.VIEW_DESTROYED, ScreenStates.DESTROYED)
                    }

                    ScreenStates.STARTED, ScreenStates.PAUSED -> {
                        applyStates(ScreenStates.STOPPED, ScreenStates.VIEW_DESTROYED, ScreenStates.DESTROYED)
                    }

                    ScreenStates.RESUMED -> {
                        applyStates(ScreenStates.PAUSED, ScreenStates.STOPPED, ScreenStates.VIEW_DESTROYED, ScreenStates.DESTROYED)
                    }

                    ScreenStates.VIEW_DESTROYED -> {
                        applyStates(ScreenStates.DESTROYED)
                    }

                    ScreenStates.DESTROYED -> {
                    }
                }
            }

            else -> applyStates(wishingState)
        }
    }

    private fun applyStates(vararg states: ScreenStates) {
        for (state in states) {
            if (state != ScreenStates.DESTROYED && parentState.isCompletelyDestroyed) continue

            if (allowedStateTransition[screenState.currentState]?.contains(state) ?: false) {
                widgetScreenEventDelegateManager.sendEvent<ScreenEvent, ScreenEventDelegate, Unit>(eventsMap[state]!!)
                screenStateEvents[state]?.invoke()
            }
        }
    }

    private fun destroy() {
        widgetScreenEventDelegateManager.destroy()
    }
}