package ru.surfstudio.android.mvp.binding.rx.sample.react.base.hub

import android.util.Log
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import ru.surfstudio.android.core.mvp.binding.react.event.Event
import ru.surfstudio.android.core.mvp.binding.react.event.hub.RxEventHub
import ru.surfstudio.android.core.mvp.binding.react.event.lifecycle.LifecycleEventCreator
import ru.surfstudio.android.core.mvp.binding.react.event.lifecycle.LifecycleEventHub
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
) : RxEventHub<T>, LifecycleEventHub<T> {

    init {
        screenEventDelegate.registerDelegate(this)
    }

    private val bus = PublishRelay.create<T>()

    override fun accept(t: T?) {
        emitEvent(t ?: return)
    }

    override fun emitEvent(event: T) {
        Log.d("111111", "event: $event")
        bus.accept(event)
    }

    override fun observeEvents(): Observable<T> = bus.hide()
}
