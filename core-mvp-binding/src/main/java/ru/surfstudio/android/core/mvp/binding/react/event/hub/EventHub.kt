package ru.surfstudio.android.core.mvp.binding.react.event.hub

import ru.surfstudio.android.core.mvp.binding.react.event.Event

interface EventHub {

    fun <T : Event> emitEvent(event: T)
}