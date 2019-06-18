package ru.surfstudio.android.core.mvp.binding.react.ui

import io.reactivex.Observable
import ru.surfstudio.android.core.mvp.binding.react.event.Event
import ru.surfstudio.android.core.mvp.binding.react.event.hub.EventHub
import ru.surfstudio.android.core.mvp.binding.react.event.hub.RxEventHub
import ru.surfstudio.android.core.mvp.binding.rx.ui.BindableRxView

interface BaseReactView : BindableRxView {

    fun <T : Event> Observable<out T>.sendTo(hub: RxEventHub<T>) =
            this as Observable<T> bindTo hub

    fun <T : Event> T.sendTo(hub: EventHub<T>) = hub.emitEvent(this)
}