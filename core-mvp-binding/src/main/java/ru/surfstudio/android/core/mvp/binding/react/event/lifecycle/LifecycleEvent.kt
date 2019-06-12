package ru.surfstudio.android.core.mvp.binding.react.event.lifecycle

import ru.surfstudio.android.core.mvp.binding.react.event.Event

enum class LifecycleEvent : Event {
    CREATE,
    VIEW_CREATE,
    START,
    RESUME,
    PAUSE,
    STOP,
    VIEW_DESTROY,
    DESTROY
}