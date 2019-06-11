package ru.surfstudio.android.mvp.binding.rx.sample.react.event

import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import ru.surfstudio.android.core.mvp.binding.react.event.Event
import ru.surfstudio.android.core.mvp.binding.react.event.hub.EventHub
import ru.surfstudio.android.core.mvp.binding.react.event.hub.RxEventHub
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Inject

@PerScreen
class EventManager @Inject constructor() : RxEventHub {

    private val bus = PublishRelay.create<Event>()

    override fun accept(t: Event?) {
        emitEvent(t ?: return)
    }

    override fun <T : Event> emitEvent(event: T) {
        bus.accept(event)
    }

    override fun observeEvents(): Observable<Event> = bus.hide()
}