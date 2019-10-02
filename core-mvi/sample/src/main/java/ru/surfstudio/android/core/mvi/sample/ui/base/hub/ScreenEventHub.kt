package ru.surfstudio.android.core.mvi.sample.ui.base.hub

import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.event.hub.RxEventHub
import ru.surfstudio.android.core.mvi.event.lifecycle.LifecycleEventCreator
import ru.surfstudio.android.core.mvi.event.lifecycle.LifecycleEventHub
import ru.surfstudio.android.core.mvi.sample.ui.base.hub.dependency.ScreenEventHubDependency
import ru.surfstudio.android.core.ui.state.ScreenState
import ru.surfstudio.android.logger.Logger

/**
 * Реализация [EventHub] для экрана.
 * Следует переопределить на проекте, если необходимо иное поведение.
 *
 * @param lifecycleEventCreator создатель [LifecycleEvent]
 */
class ScreenEventHub<T : Event>(
        dependency: ScreenEventHubDependency,
        override val lifecycleEventCreator: LifecycleEventCreator<T>? = null
) : RxEventHub<T>, LifecycleEventHub<T, Observable<T>> {

    init {
        dependency.screenEventDelegate.registerDelegate(this)
    }

    override val screenState: ScreenState = dependency.screenState

    private val bus = PublishRelay.create<T>()

    override fun accept(t: T?) {
        emit(t ?: return)
    }

    override fun emit(event: T) {
        //Log events
        Logger.d("ScreenEventHub emit: $event")
        bus.accept(event)
    }

    override fun observe(): Observable<T> = bus.hide()
}
