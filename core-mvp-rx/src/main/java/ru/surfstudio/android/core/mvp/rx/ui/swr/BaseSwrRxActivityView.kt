package ru.surfstudio.android.core.mvp.rx.ui.swr

import androidx.annotation.CallSuper
import ru.surfstudio.android.core.mvp.rx.ui.RxModel
import ru.surfstudio.android.core.mvp.view.CoreView

abstract class BaseSwrRxActivityView<M> : CoreView, SwrRxView<M> where M : RxModel, M : HasSwrState {
    @CallSuper
    override fun bind(sm: M) {
        bindSwipeRefreshState(sm)
    }
}