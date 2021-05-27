package ru.surfstudio.android.mvp.widget.event

import android.view.View
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
import ru.surfstudio.android.core.ui.event.lifecycle.stop.OnStopDelegate
import ru.surfstudio.android.core.ui.event.lifecycle.stop.OnStopEvent
import ru.surfstudio.android.core.ui.event.lifecycle.view.destroy.OnViewDestroyDelegate
import ru.surfstudio.android.core.ui.event.lifecycle.view.destroy.OnViewDestroyEvent
import ru.surfstudio.android.core.ui.state.LifecycleStage
import ru.surfstudio.android.core.ui.state.ScreenState
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
 * TODO Пересмотреть логику реакции на эвенты, приодящие со стороны родителя и виджета,
 * TODO выработать более унифицированный, декларативный и понятный подход.
 *
 * @param parentState состояние родителя
 * @param widgetScreenEventDelegateManager делегат менеджер виджета
 * @param screenState состояние виджета
 */
class WidgetLifecycleManager(
        private val screenState: WidgetScreenState,
        private val parentState: ScreenState,
        private val widgetScreenEventDelegateManager: WidgetScreenEventDelegateManager,
        private val parentScreenEventDelegateManager: ScreenEventDelegateManager
) : OnCompletelyDestroyDelegate,
        OnViewReadyDelegate,
        OnStartDelegate,
        OnResumeDelegate,
        OnPauseDelegate,
        OnStopDelegate,
        OnViewDestroyDelegate {

    private var widgetViewDelegate: WeakReference<WidgetViewDelegate>? = null

    /**
     * Резолвер следующего этапа ЖЦ виджета, в колбеке метод применяющий состояние
     */
    private val stageResolver = StageResolver(screenState, parentState, ::applyStage)

    //действия с скринстейт по состоянию виджета
    private val screenStateEvents = mapOf(
            LifecycleStage.VIEW_CREATED to { screenState.onViewReady() },
            LifecycleStage.STARTED to { screenState.onStart() },
            LifecycleStage.RESUMED to { screenState.onResume() },
            LifecycleStage.PAUSED to { screenState.onPause() },
            LifecycleStage.STOPPED to { screenState.onStop() },
            LifecycleStage.VIEW_DESTROYED to { screenState.onViewDestroy() },
            LifecycleStage.COMPLETELY_DESTROYED to { screenState.onCompletelyDestroy() }
    )

    //эвенты для состояний
    private val eventsMap = mapOf(
            LifecycleStage.VIEW_CREATED to OnViewReadyEvent(),
            LifecycleStage.STARTED to OnStartEvent(),
            LifecycleStage.RESUMED to OnResumeEvent(),
            LifecycleStage.PAUSED to OnPauseEvent(),
            LifecycleStage.STOPPED to OnStopEvent(),
            LifecycleStage.VIEW_DESTROYED to OnViewDestroyEvent(),
            LifecycleStage.COMPLETELY_DESTROYED to OnCompletelyDestroyEvent()
    )

    init {
        parentScreenEventDelegateManager.registerDelegate(this)
    }

    fun onCreate(widgetView: View, coreWidgetView: CoreWidgetViewInterface, widgetViewDelegate: WidgetViewDelegate) {
        // Необходимо для соблюдения правильного потока событий в RecyclerView со включенными анимациями:
        // Для анимирования изменения элементов, RecyclerView создает 2 ViewHolder с двумя разными виджетами,
        // у которых, при этом, одинаковый id.
        // Из-за этого, вместо жизненного цикла
        //          1.onCreate, 1.onDestroy 2.onCreate
        // вызывается:
        //          1.onCreate, 2.onCreate, 2.onDestroy
        //
        // Чтобы предотвратить подобное поведение, если у нас есть предыдущий делегат,
        // явно посылаем эвенты onPause, onStop, onViewDestroyed перед созданием нового,
        // и помечаем делегат как уничтоженный.
        this.widgetViewDelegate?.get()?.let {
            stageResolver.pushState(LifecycleStage.VIEW_DESTROYED, StageSource.LIFECYCLE_MANAGER)
            it.setViewDestroyedForcibly()
        }

        this.widgetViewDelegate = WeakReference(widgetViewDelegate)

        screenState.onCreate(widgetView, coreWidgetView)
    }

    fun onViewReady(source: StageSource) {
        stageResolver.pushState(LifecycleStage.VIEW_CREATED, source)
        parentScreenEventDelegateManager.sendUnhandledEvents()
    }

    fun onStart(source: StageSource) {
        stageResolver.pushState(LifecycleStage.STARTED, source)
    }

    fun onResume(source: StageSource) {
        stageResolver.pushState(LifecycleStage.RESUMED, source)
    }

    fun onPause(source: StageSource) {
        stageResolver.pushState(LifecycleStage.PAUSED, source)
    }

    fun onStop(source: StageSource) {
        stageResolver.pushState(LifecycleStage.STOPPED, source)
    }

    fun onViewDestroy(source: StageSource) {
        stageResolver.pushState(LifecycleStage.VIEW_DESTROYED, source)
    }

    fun onCompletelyDestroy(source: StageSource) {
        stageResolver.pushState(LifecycleStage.COMPLETELY_DESTROYED, source)
        destroy()

        widgetViewDelegate?.get()?.onCompletelyDestroy()
    }

    override fun onViewReady() {
        onViewReady(StageSource.PARENT)
    }

    override fun onStart() {
        onStart(StageSource.PARENT)
    }

    override fun onResume() {
        onResume(StageSource.PARENT)
    }

    override fun onPause() {
        onPause(StageSource.PARENT)
    }

    override fun onStop() {
        onStop(StageSource.PARENT)
    }

    override fun onViewDestroy() {
        onViewDestroy(StageSource.PARENT)
    }

    override fun onCompletelyDestroy() {
        onCompletelyDestroy(StageSource.PARENT)
    }


    /**
     * Применяет события к виджету, если они разрешены текущим состоянием
     * Также посылает эвент с текущим событием.
     * @param state
     */
    fun applyStage(state: LifecycleStage) {
        if (state == LifecycleStage.CREATED) return
        widgetScreenEventDelegateManager.sendEvent<ScreenEvent, ScreenEventDelegate, Unit>(eventsMap[state])
        screenStateEvents[state]?.invoke()
    }

    private fun destroy() {
        widgetScreenEventDelegateManager.destroy()
        widgetViewDelegate?.clear()
    }
}