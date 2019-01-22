package ru.surfstudio.android.core.mvp.rx.ui.lds

import io.reactivex.Observable
import ru.surfstudio.android.core.mvp.loadstate.LoadStateInterface
import ru.surfstudio.android.core.mvp.rx.ui.CoreRxPresenter
import ru.surfstudio.android.core.mvp.rx.ui.RxModel

interface LdsRxPresenter<M> : CoreRxPresenter<M> where M : RxModel, M : HasLoadState {

    fun applyLoadState(new: LoadStateInterface) {
        getRxModel().loadState.accept(new)
    }

    fun <T> Observable<T>.applyLoadState(new: LoadStateInterface) = map {
        this@LdsRxPresenter.applyLoadState(new)
        it
    }

    fun <T> Observable<T>.applyLoadState(loadStateFromDataAction: (T) -> LoadStateInterface) = map {
        this@LdsRxPresenter.applyLoadState(loadStateFromDataAction(it))
        it
    }

}