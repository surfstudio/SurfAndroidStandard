package ru.surfstudio.android.core.mvi.ui.holder

/**
 * Класс, содержащий состояние экрана для [Reducer]
 */
abstract class ReducerStateHolder<S> {
    internal abstract var state: S
}