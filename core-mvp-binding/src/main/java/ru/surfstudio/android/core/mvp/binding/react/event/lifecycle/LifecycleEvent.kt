package ru.surfstudio.android.core.mvp.binding.react.event.lifecycle

import ru.surfstudio.android.core.mvp.binding.react.event.Event

/**
 * Событие изменение жизненного цикла экрана
 */
interface LifecycleEvent : Event {
    var stage: LifecycleStage
}