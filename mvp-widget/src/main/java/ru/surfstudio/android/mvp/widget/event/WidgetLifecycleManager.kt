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
import java.lang.ref.WeakReference

/**
 * Управляет ЖЦ виджета
 *
 * Новое состояние виджета расчитывается в зависимости от текщего состояния виджета и его родителя.
 * allowedStateTransition - возможные переходы виджета из текущего состояния.
 *
 * STARTED/STOPPED, RESUMED/PAUSED  - управляются родителем, либо посылаются искуственно(см. pushState)
 *
 * TODO: тесты метода pushState
 *
 * @param parentState состояние родителя
 * @param widgetScreenEventDelegateManager делегат менеджер виджета
 * @param screenState состояние виджета
 */
class WidgetLifecycleManager(
        private val screenState: WidgetScreenState,
        private val parentState: ScreenState,
        private val widgetScreenEventDelegateManager: WidgetScreenEventDelegateManager,
        parentScreenEventDelegateManager: ScreenEventDelegateManager
) : OnCompletelyDestroyDelegate,
        OnStartDelegate,
        OnResumeDelegate,
        OnPauseDelegate,
        OnStopDelegate,
        OnViewDestroyDelegate {

    private lateinit var widgetViewDelegate: WeakReference<WidgetViewDelegate>

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

    //действия с скринстейт по состоянию виджета
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

    init {
        parentScreenEventDelegateManager.registerDelegate(this)
    }

    fun onCreate(widgetView: View, coreWidgetView: CoreWidgetViewInterface, widgetViewDelegate: WidgetViewDelegate) {
        screenState.onCreate(widgetView, coreWidgetView)

        this.widgetViewDelegate = WeakReference(widgetViewDelegate)
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

        widgetViewDelegate.get()?.onCompletelyDestroy()
    }

    /**
     * Приводит виджет в состояние в зависимости от текущих родительского, виджета и желаемого
     * @param wishingState желаемое состояние
     */
    private fun pushState(wishingState: ScreenStates) {
        when {

            //Первые два кейса - поведение виджета в ресайклере. Необходимо послать STARTED/RESUMED и PAUSED/STOPPED
            //на attach/detach
            wishingState == ScreenStates.VIEW_DESTROYED && parentState.currentState == ScreenStates.RESUMED -> {
                applyStates(ScreenStates.PAUSED, ScreenStates.STOPPED, ScreenStates.VIEW_DESTROYED)
            }

            wishingState == ScreenStates.VIEW_READY && parentState.currentState == ScreenStates.RESUMED -> {
                applyStates(ScreenStates.VIEW_READY, ScreenStates.STARTED, ScreenStates.RESUMED)
            }

            // Когда уничтожаем виджет, необходимо в ручную послать предыдущие события
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

    /**
     * Применяет события к виджету, если они разрешены текущим состоянием
     * Также посылает эвент с текущим событием.
     * @param states
     */
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