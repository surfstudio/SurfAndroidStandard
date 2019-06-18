package ru.surfstudio.android.core.mvp.binding.react.event.lifecycle

/**
 * Стадии жизненного цикла.
 */
enum class LifecycleStage {
    CREATE,
    VIEW_CREATE,
    START,
    RESUME,
    PAUSE,
    STOP,
    VIEW_DESTROY,
    DESTROY
}