package ru.surfstudio.android.core.mvp.binding.react.ui

import io.reactivex.Observable
import ru.surfstudio.android.core.mvp.binding.react.event.Event
import ru.surfstudio.android.core.mvp.binding.react.event.hub.holder.EventHubHolder
import ru.surfstudio.android.core.mvp.binding.react.event.hub.RxEventHub
import ru.surfstudio.android.core.mvp.binding.react.reactor.StatefulReactor
import ru.surfstudio.android.core.mvp.binding.rx.ui.BindableRxView

interface BaseReactView : BindableRxView, EventHubHolder {

    override val hub: RxEventHub

    fun getReactors(): Array<out StatefulReactor>

    fun <T> Observable<T>.mapAndSend(eventTransformer: (T) -> Event) =
            this.map(eventTransformer) bindTo hub

    fun <T> Observable<T>.send(event: Event) =
            this.map { event } bindTo hub

    fun send(event: Event) =
            hub.emitEvent(event)

}