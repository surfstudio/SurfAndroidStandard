package ru.surfstudio.android.core.mvp.binding.react.event.hub

import io.reactivex.Observable
import io.reactivex.functions.Consumer
import ru.surfstudio.android.core.mvp.binding.react.event.Event

/**
 * [EventHub] с поддержкой Rx.
 */
interface RxEventHub<T : Event> : EventHub<T>, Consumer<T> {

    fun observeEvents(): Observable<T>
}