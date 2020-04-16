package ru.surfstudio.android.core.mvi.ui.reducer

import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.ui.reactor.Reactor
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.State

/**
 * [Reducer] из терминологии Redux:
 *
 * Этот класс служит для изменения текущего состояние экрана [State],
 * при реакции на событие [Event].
 *
 * @see <a href="Reducers documentation">https://redux.js.org/basics/reducers</a>
 */
interface Reducer<E : Event, S> : Reactor<E, State<S>> {

    override fun react(sh: State<S>, event: E) {
        val oldState = sh.value
        val newState = reduce(oldState, event)
        if (isStateChanged(oldState, newState)) {
            sh.accept(newState)
        }
    }

    /**
     * Метод, в котором происходит определение того, изменилось ли состояние
     * после того, как произошла реакция на событие.
     *
     * Это происходит, чтобы StateHolder уведомлял своих подписчиков
     * только при реальной смене состояния.
     *
     * Можно переопределить, если необходима сложная проверка или настраиваемое поведение.
     */
    fun isStateChanged(oldState: S, newState: S) = oldState != newState

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