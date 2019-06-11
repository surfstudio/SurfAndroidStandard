package ru.surfstudio.android.core.mvp.binding.react.middleware

import ru.surfstudio.android.core.mvp.binding.react.event.Event

interface MiddleWareHolder {

    fun <T : Event> accept(event: T)
}