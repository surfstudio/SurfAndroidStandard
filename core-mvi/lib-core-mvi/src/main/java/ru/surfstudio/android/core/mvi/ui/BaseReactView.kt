package ru.surfstudio.android.core.mvi.ui

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.event.hub.RxEventHub
import ru.surfstudio.android.core.mvp.binding.rx.relation.Relation
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.StateTarget
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.VIEW
import ru.surfstudio.android.core.mvp.binding.rx.ui.BindableRxView

/**
 * Базовая реактивная вью, которая может отправлять события в RxEventHub.
 */
interface BaseReactView : BindableRxView {

    /**
     * Send event each time [Observable] emits new value.
     *
     * @param hub hub to receive events
     */
    fun <T : Event> Observable<out T>.emit(hub: RxEventHub<T>) =
            this as Observable<T> bindTo hub

    /**
     * Send event each time [Observable] emits new value.
     *
     * @param event     event to send
     * @param hub       hub to receive events
     *
     */
    fun <T, E : Event> Observable<T>.emit(event: E, hub: RxEventHub<E>) =
            this.map { event } bindTo hub

    /**
     * Send event to hub.
     *
     * @param hub       hub to receive events
     */
    fun <T : Event> T.emit(hub: RxEventHub<T>) = hub.emit(this)

    /**
     * Bind observable to a consumer with blocking.
     *
     * You should use this method if you have already have data in your state at the time the
     * screen created, for example, it's extracted from the route and placed to a State in constructor.
     *
     * First value will be consumed before subscription, right after function invocation.
     * If the value isn't present in observable - thread will be blocked until the first emission.
     *
     * All emissions after first will be processed in the usual way.
     *
     * This method is needed because the default implementation of bindTo in View
     * adds all the messages to the end of the MessageQueue,
     * and they're handled screen's onResume events.
     *
     * Because of this, the view first renders the screen without data,
     * and the data appears only after a while, causing little lag, visible to user.
     *
     */
    infix fun <T> Observable<T>.bindToBlocking(consumer: (T) -> Unit) {
        val firstItem = this.blockingFirst()
        consumer.invoke(firstItem)
        this.skip(1) bindTo consumer
    }

    /**
     * @see [Observable.bindToBlocking]
     */
    infix fun <T> Relation<T, *, VIEW>.bindToBlocking(consumer: (T) -> Unit) {
        observable.bindToBlocking(consumer)
    }

}