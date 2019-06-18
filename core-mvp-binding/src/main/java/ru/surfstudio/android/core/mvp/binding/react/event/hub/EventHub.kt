package ru.surfstudio.android.core.mvp.binding.react.event.hub

import ru.surfstudio.android.core.mvp.binding.react.event.Event

/**
 * Базовый типизированный EventHub.
 */
interface EventHub<T: Event> {

    fun emitEvent(event: T)
}