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
import ru.surfstudio.android.core.ui.state.LifecycleStage
import ru.surfstudio.android.mvp.widget.delegate.WidgetViewDelegate
import ru.surfstudio.android.mvp.widget.event.delegate.WidgetScreenEventDelegateManager
import ru.surfstudio.android.mvp.widget.state.WidgetScreenState
import ru.surfstudio.android.mvp.widget.view.CoreWidgetViewInterface
import java.lang.IllegalStateException
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
            LifecycleStage.CREATED to listOf(LifecycleStage.VIEW_READY, LifecycleStage.DESTROYED),
            LifecycleStage.VIEW_READY to listOf(LifecycleStage.STARTED, LifecycleStage.VIEW_DESTROYED),
            LifecycleStage.STARTED to listOf(LifecycleStage.RESUMED, LifecycleStage.STOPPED),
            LifecycleStage.RESUMED to listOf(LifecycleStage.PAUSED),
            LifecycleStage.PAUSED to listOf(LifecycleStage.RESUMED, LifecycleStage.STOPPED),
            LifecycleStage.STOPPED to listOf(LifecycleStage.STARTED, LifecycleStage.VIEW_DESTROYED),
            LifecycleStage.VIEW_DESTROYED to listOf(LifecycleStage.VIEW_READY, LifecycleStage.DESTROYED),
            LifecycleStage.DESTROYED to listOf()
    )

    //действия с скринстейт по состоянию виджета
    private val screenStateEvents = mapOf(
            LifecycleStage.VIEW_READY to { screenState.onViewReady() },
            LifecycleStage.STARTED to { screenState.onStart() },
            LifecycleStage.RESUMED to { screenState.onResume() },
            LifecycleStage.PAUSED to { screenState.onPause() },
            LifecycleStage.STOPPED to { screenState.onStop() },
            LifecycleStage.VIEW_DESTROYED to { screenState.onViewDestroy() },
            LifecycleStage.DESTROYED to { screenState.onDestroy() }
    )

    //эвенты для состояний
    private val eventsMap = mapOf(
            LifecycleStage.VIEW_READY to OnViewReadyEvent(),
            LifecycleStage.STARTED to OnStartEvent(),
            LifecycleStage.RESUMED to OnResumeEvent(),
            LifecycleStage.PAUSED to OnPauseEvent(),
            LifecycleStage.STOPPED to OnStopEvent(),
            LifecycleStage.VIEW_DESTROYED to OnViewDestroyEvent(),
            LifecycleStage.DESTROYED to OnCompletelyDestroyEvent()
    )

    init {
        parentScreenEventDelegateManager.registerDelegate(this)
    }

    fun onCreate(widgetView: View, coreWidgetView: CoreWidgetViewInterface, widgetViewDelegate: WidgetViewDelegate) {
        screenState.onCreate(widgetView, coreWidgetView)

        this.widgetViewDelegate = WeakReference(widgetViewDelegate)
    }

    fun onViewReady() {
        if (parentState.lifecycleStage.ordinal < LifecycleStage.VIEW_READY.ordinal) {
            throw IllegalStateException("Parent is not ready")
        }

        pushState(LifecycleStage.VIEW_READY)
    }

    override fun onStart() {
        pushState(LifecycleStage.STARTED)
    }

    override fun onResume() {
        pushState(LifecycleStage.RESUMED)
    }

    override fun onPause() {
        pushState(LifecycleStage.PAUSED)
    }

    override fun onStop() {
        pushState(LifecycleStage.STOPPED)
    }

    override fun onViewDestroy() {
        pushState(LifecycleStage.VIEW_DESTROYED)
    }

    override fun onCompletelyDestroy() {
        pushState(LifecycleStage.DESTROYED)
        destroy()

        widgetViewDelegate.get()?.onCompletelyDestroy()
    }

    /**
     * Приводит виджет в состояние в зависимости от текущих родительского, виджета и желаемого
     * @param wishingState желаемое состояние
     */
    private fun pushState(wishingState: LifecycleStage) {
        when {

            //Первые два кейса - поведение виджета в ресайклере. Необходимо послать STARTED/RESUMED и PAUSED/STOPPED
            //на attach/detach
            wishingState == LifecycleStage.VIEW_DESTROYED && parentState.lifecycleStage == LifecycleStage.RESUMED -> {
                applyStates(LifecycleStage.PAUSED, LifecycleStage.STOPPED, LifecycleStage.VIEW_DESTROYED)
            }

            wishingState == LifecycleStage.VIEW_READY && parentState.lifecycleStage == LifecycleStage.RESUMED -> {
                applyStates(LifecycleStage.VIEW_READY, LifecycleStage.STARTED, LifecycleStage.RESUMED)
            }

            // Когда уничтожаем виджет, необходимо в ручную послать предыдущие события
            wishingState == LifecycleStage.DESTROYED -> {
                when (screenState.lifecycleStage!!) {

                    LifecycleStage.CREATED, LifecycleStage.VIEW_READY, LifecycleStage.STOPPED -> {
                        applyStates(LifecycleStage.VIEW_DESTROYED, LifecycleStage.DESTROYED)
                    }

                    LifecycleStage.STARTED, LifecycleStage.PAUSED -> {
                        applyStates(LifecycleStage.STOPPED, LifecycleStage.VIEW_DESTROYED, LifecycleStage.DESTROYED)
                    }

                    LifecycleStage.RESUMED -> {
                        applyStates(LifecycleStage.PAUSED, LifecycleStage.STOPPED, LifecycleStage.VIEW_DESTROYED, LifecycleStage.DESTROYED)
                    }

                    LifecycleStage.VIEW_DESTROYED -> {
                        applyStates(LifecycleStage.DESTROYED)
                    }

                    LifecycleStage.DESTROYED -> {
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
    private fun applyStates(vararg states: LifecycleStage) {
        for (state in states) {
            if (state != LifecycleStage.DESTROYED && parentState.isCompletelyDestroyed) continue

            if (allowedStateTransition[screenState.lifecycleStage]?.contains(state) ?: false) {
                widgetScreenEventDelegateManager.sendEvent<ScreenEvent, ScreenEventDelegate, Unit>(eventsMap[state]!!)
                screenStateEvents[state]?.invoke()
            }
        }
    }

    private fun destroy() {
        widgetScreenEventDelegateManager.destroy()
    }
}