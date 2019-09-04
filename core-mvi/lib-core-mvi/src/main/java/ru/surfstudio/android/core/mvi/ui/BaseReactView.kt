package ru.surfstudio.android.core.mvi.ui

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.event.hub.RxEventHub
import ru.surfstudio.android.core.mvp.binding.rx.ui.BindableRxView

/**
 * Базовая реактивная вью, которая может отправлять события в RxEventHub.
 */
interface BaseReactView : BindableRxView {

    /**
     * Отправка события каждый раз, когда [Observable] эмитит новое значение.
     *
     * @param hub хаб, в который отправляется событие
     */
    fun <T : Event> Observable<out T>.sendTo(hub: RxEventHub<T>) =
            this as Observable<T> bindTo hub

    /**
     * Отправка события каждый раз, когда [Observable] эмитит новое значение.
     *
     * @param event     отправляемое событие
     * @param hub       хаб, в который отправляется событие
     */
    fun <T, E : Event> Observable<T>.send(event: E, hub: RxEventHub<E>) =
            this.map { event } bindTo hub

    /**
     * Отправка события.
     *
     * @param hub хаб, в который отправляется событие
     */
    fun <T : Event> T.sendTo(hub: RxEventHub<T>) = hub.emit(this)
}