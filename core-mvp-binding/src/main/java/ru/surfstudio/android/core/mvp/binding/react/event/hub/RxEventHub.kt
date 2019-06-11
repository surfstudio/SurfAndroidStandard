package ru.surfstudio.android.core.mvp.binding.react.event.hub

import io.reactivex.Observable
import io.reactivex.functions.Consumer
import ru.surfstudio.android.core.mvp.binding.react.event.Event

interface RxEventHub : EventHub, Consumer<Event> {

    fun observeEvents(): Observable<Event>
}