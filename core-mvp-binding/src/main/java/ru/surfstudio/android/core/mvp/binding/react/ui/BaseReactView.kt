package ru.surfstudio.android.core.mvp.binding.react.ui

import io.reactivex.Observable
import ru.surfstudio.android.core.mvp.binding.react.event.Event
import ru.surfstudio.android.core.mvp.binding.react.event.hub.holder.EventHubHolder
import ru.surfstudio.android.core.mvp.binding.react.event.hub.RxEventHub
import ru.surfstudio.android.core.mvp.binding.react.reactor.Feature
import ru.surfstudio.android.core.mvp.binding.rx.ui.BindableRxView

interface BaseReactView : BindableRxView, EventHubHolder {

    override val hub: RxEventHub

    fun getFeatures(): Array<out Feature>

    fun <T> Observable<T>.sendEvent(eventTransformer: (T) -> Event) =
            this.map(eventTransformer) bindTo hub

    fun <T> Observable<T>.sendEvent(event: Event) =
            this.map { event } bindTo hub

    fun sendEvent(event: Event) =
            hub.emitEvent(event)

}