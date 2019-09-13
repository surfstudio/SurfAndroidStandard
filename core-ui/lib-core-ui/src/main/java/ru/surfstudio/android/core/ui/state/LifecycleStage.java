package ru.surfstudio.android.core.ui.state;

/**
 * Этап ЖЦ вью
 */
public enum LifecycleStage {
    CREATED,
    VIEW_CREATED,
    STARTED,
    RESUMED,
    PAUSED,
    STOPPED,
    VIEW_DESTROYED,
    COMPLETELY_DESTROYED // == completely destroyed
}
