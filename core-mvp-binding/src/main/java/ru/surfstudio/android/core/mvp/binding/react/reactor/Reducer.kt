package ru.surfstudio.android.core.mvp.binding.react.reactor

import androidx.annotation.CallSuper
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import ru.surfstudio.android.core.mvp.binding.react.event.Event
import ru.surfstudio.android.core.mvp.binding.rx.relation.Related
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.PRESENTER

/**
 * [Reducer] из терминологии Redux:
 *
 * Этот класс служит для изменения текущего состояние экрана [StateHolder],
 * при реакции на событие [Event].
 *
 * @see <a href="Reducers documentation">https://redux.js.org/basics/reducers</a>
 */
interface Reducer<T : Event, H : StateHolder> : Related<PRESENTER> {

    /**
     * Трансформация состояния экрана с помощью поступающих событий от Ui или от слоя данных.
     *
     * @param state состояние экрана
     * @param event событие
     *
     * @return обновленное состояние экрана
     */
    fun reduce(state: H, event: T): H

    override fun relationEntity() = PRESENTER

    @CallSuper
    override fun <T> subscribe(observable: Observable<T>, onNext: Consumer<T>, onError: (Throwable) -> Unit): Disposable {
        throw NotImplementedError("Reducer cant manage subscription lifecycle")
    }

}