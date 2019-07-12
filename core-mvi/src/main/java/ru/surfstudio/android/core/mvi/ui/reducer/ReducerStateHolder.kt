package ru.surfstudio.android.core.mvi.ui.reducer

/**
 * Класс, содержащий состояние экрана для [Reducer]
 */
interface ReducerStateHolder<S> {
    var state: S
}