package ru.surfstudio.android.core.mvp.binding.react.event.hub

import android.util.Log
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import ru.surfstudio.android.core.mvp.binding.react.event.Event
import ru.surfstudio.android.core.mvp.binding.react.event.lifecycle.LifecycleEventHub
import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager
import ru.surfstudio.android.core.ui.state.ScreenState

/**
 * Возможная имплементация EventHub.
 * Следует переопределить на проекте, если необходимо иное поведение.
 */
open class EventHubImpl(
        override val screenState: ScreenState,
        screenEventDelegate: ScreenEventDelegateManager
) : RxEventHub, LifecycleEventHub {

    init {
        screenEventDelegate.registerDelegate(this)
    }

    private val bus = PublishRelay.create<Event>()

    override fun accept(t: Event?) {
        emitEvent(t ?: return)
    }

    override fun <T : Event> emitEvent(event: T) {
        Log.d("111111", "event: $event")
        bus.accept(event)
    }

    override fun observeEvents(): Observable<Event> = bus.hide()
}