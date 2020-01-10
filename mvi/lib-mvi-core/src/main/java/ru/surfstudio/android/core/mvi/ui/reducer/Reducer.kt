package ru.surfstudio.android.core.mvi.ui.reducer

import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.ui.holder.ReducerStateHolder
import ru.surfstudio.android.core.mvi.ui.reactor.Reactor

/**
 * [Reducer] из терминологии Redux:
 *
 * Этот класс служит для изменения текущего состояние экрана [StateModel],
 * при реакции на событие [Event].
 *
 * @see <a href="Reducers documentation">https://redux.js.org/basics/reducers</a>
 */
interface Reducer<
        E : Event,
        S,
        H : ReducerStateHolder<S>> : Reactor<E, H> {

    override fun react(sh: H, event: E) {
        val newState = reduce(sh.state, event)
        sh.state = newState
    }

    /**
     * Трансформация состояния экрана с помощью поступающих событий от Ui или от слоя данных.
     *
     * @param state состояние экрана
     * @param event событие
     *
     * @return обновленное состояние экрана
     */
    fun reduce(state: S, event: E): S
}