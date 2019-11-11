package ru.surfstudio.android.core.mvi.event.hub.owner

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.event.hub.RxEventHub
import ru.surfstudio.android.core.mvi.ui.BaseReactView

/**
 * Интерфейс-маркер класса, содержащего один [RxEventHub].
 *
 * Содержит в себе экстеншны для удобного эмита событий в хаб.
 */
interface SingleHubOwner<T : Event> : BaseReactView {

    val hub: RxEventHub<T>

    /**
     * Отправка события каждый раз, когда [Observable] эмитит новое значение.
     *
     * @param hub хаб, в который отправляется событие
     */
    fun Observable<out T>.emit() =
            this as Observable<T> bindTo hub

    /**
     * Отправка события каждый раз, когда [Observable] эмитит новое значение.
     *
     * @param event     отправляемое событие
     * @param hub       хаб, в который отправляется событие
     */
    fun <R> Observable<R>.emit(event: T) =
            this.map { event } bindTo hub

    /**
     * Отправка события каждый раз, когда [Observable] эмитит новое значение.
     *
     * @param event     отправляемое событие
     * @param hub       хаб, в который отправляется событие
     */
    fun <R> Observable<R>.emit(eventTransformer: (R) -> T) =
            this.map(eventTransformer) bindTo hub

    /**
     * Отправка события.
     *
     * @param hub хаб, в который отправляется событие
     */
    fun T.emit() = hub.emit(this)

}