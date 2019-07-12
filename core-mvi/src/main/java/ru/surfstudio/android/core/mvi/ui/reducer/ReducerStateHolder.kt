package ru.surfstudio.android.core.mvi.ui.reducer

import ru.surfstudio.android.core.mvi.ui.reactor.StateHolder

/**
 * Класс, содержащий состояние экрана для [Reducer]
 */
interface ReducerStateHolder<S> : StateHolder {
    var state: S
}