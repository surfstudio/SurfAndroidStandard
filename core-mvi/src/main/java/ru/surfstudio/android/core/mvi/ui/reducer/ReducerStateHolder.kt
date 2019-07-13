package ru.surfstudio.android.core.mvi.ui.reducer

/**
 * Класс, содержащий состояние экрана для [Reducer]
 */
abstract class ReducerStateHolder<S> {
    internal abstract var state: S
}