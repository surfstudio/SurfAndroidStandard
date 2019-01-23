package ru.surfstudio.android.core.mvp.rx.ui.swr

import io.reactivex.Observable
import ru.surfstudio.android.core.mvp.model.state.SwipeRefreshState
import ru.surfstudio.android.core.mvp.rx.ui.CoreRxPresenter
import ru.surfstudio.android.core.mvp.rx.ui.RxModel

interface SwrRxPresenter<M>: CoreRxPresenter<M> where M : RxModel, M : HasSwrState {

    fun applySwrState(new: SwipeRefreshState) {
        model.swipeRefreshState.accept(new)
    }

    fun <T> Observable<T>.applySwrState(new: SwipeRefreshState) = map {
        this@SwrRxPresenter.applySwrState(new)
        it
    }

    fun <T> Observable<T>.applySwrState(swrStateFromDataAction: (T) -> SwipeRefreshState) = map {
        this@SwrRxPresenter.applySwrState(swrStateFromDataAction(it))
        it
    }

}