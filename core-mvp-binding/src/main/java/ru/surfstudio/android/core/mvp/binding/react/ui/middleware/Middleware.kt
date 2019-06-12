package ru.surfstudio.android.core.mvp.binding.react.ui.middleware

import ru.surfstudio.android.core.mvp.binding.react.event.Event

interface Middleware {
    fun accept(event: Event)
}