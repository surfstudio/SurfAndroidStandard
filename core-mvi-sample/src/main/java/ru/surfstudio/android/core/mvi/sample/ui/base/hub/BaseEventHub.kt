package ru.surfstudio.android.core.mvi.sample.ui.base.hub

import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.event.hub.RxEventHub
import ru.surfstudio.android.core.mvi.event.lifecycle.LifecycleEventCreator
import ru.surfstudio.android.core.mvi.event.lifecycle.LifecycleEventHub
import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager
import ru.surfstudio.android.core.ui.state.ScreenState

/**
 * Возможная имплементация [EventHub].
 * Следует переопределить на проекте, если необходимо иное поведение.
 *
 * @param lifecycleEventCreator создатель [LifecycleEvent]
 */
open class BaseEventHub<T : Event>(
        override val screenState: ScreenState,
        screenEventDelegate: ScreenEventDelegateManager,
        override val lifecycleEventCreator: LifecycleEventCreator<T>? = null
) : RxEventHub<T>, LifecycleEventHub<T, Observable<T>> {

    init {
        screenEventDelegate.registerDelegate(this)
    }

    private val bus = PublishRelay.create<T>()

    override fun accept(t: T?) {
        emit(t ?: return)
    }

    override fun emit(event: T) {
        //Log events
        bus.accept(event)
    }

    override fun observe(): Observable<T> = bus.hide()
}
