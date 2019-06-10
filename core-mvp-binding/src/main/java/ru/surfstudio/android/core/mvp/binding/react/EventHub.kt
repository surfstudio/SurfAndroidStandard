package ru.surfstudio.android.core.mvp.binding.react

import io.reactivex.Observable

interface EventHub {
    fun <T : Event> acceptEvent(eventObservable: Observable<T>)
    fun <T : Event> acceptEvent(event: T)
}