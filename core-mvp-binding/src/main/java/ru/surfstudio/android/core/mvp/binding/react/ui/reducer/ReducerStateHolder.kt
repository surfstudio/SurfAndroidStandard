package ru.surfstudio.android.core.mvp.binding.react.ui.reducer

import ru.surfstudio.android.core.mvp.binding.react.ui.reactor.StateHolder

/**
 * Класс, содержащий состояние экрана для [Reducer]
 */
interface ReducerStateHolder<S : ReducerState>: StateHolder {
    var state: S
}