package ru.surfstudio.android.core.mvp.rx.ui.lds

import io.reactivex.Observable
import ru.surfstudio.android.core.mvp.loadstate.LoadStateInterface
import ru.surfstudio.android.core.mvp.rx.domain.State
import ru.surfstudio.android.core.mvp.rx.ui.CoreRxPresenter
import ru.surfstudio.android.core.mvp.rx.ui.RxModel

interface LdsRxPresenter<M> : CoreRxPresenter<M> where M : RxModel, M : HasLoadState {

    /**
     * Изменение значения loadState
     * Параметр [state] следует переопределять, если модель содержит
     * в себе несколько [State] на основе LoadStateInterface,
     * и их необходимо менять независимо друг от друга
     *
     * @param new новое значение loadState
     * @param state состояние, которое принимает в себя новое значение.
     */
    fun applyLoadState(
            new: LoadStateInterface,
            state: State<LoadStateInterface> = model.loadState
    ) {
        state.accept(new)
    }

    /**
     * Изменение значения loadState внутри rx-цепочки
     *
     * @see applyLoadState
     */
    fun <T> Observable<T>.applyLoadState(
            new: LoadStateInterface,
            state: State<LoadStateInterface> = model.loadState
    ) = map {
        this@LdsRxPresenter.applyLoadState(new, state)
        it
    }

    /**
     * Изменение значения loadState внутри rx-цепочки на основе данных из цепочки
     *
     * @param loadStateFromDataAction лямбда, принимающая данные и возвращающая loadState
     * @param state [State], который необходимо изменить
     */
    fun <T> Observable<T>.applyLoadState(
            loadStateFromDataAction: (T) -> LoadStateInterface,
            state: State<LoadStateInterface> = model.loadState
    ) = map {
        this@LdsRxPresenter.applyLoadState(loadStateFromDataAction(it), state)
        it
    }

}