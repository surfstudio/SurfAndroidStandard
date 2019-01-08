package ru.surfstudio.core_mvp_rxbinding.base.ui.swr_lds

import androidx.annotation.CallSuper
import ru.surfstudio.android.core.mvp.view.CoreView

abstract class BaseLdsSwrRxActivityView<M : LdsSwrRxModel>
    : CoreView, LdsSwrBindableView<M> {

    @CallSuper
    override fun bind(sm: M) {
        bindLoadState(sm)
        bindSwipeRefreshState(sm)
    }
}