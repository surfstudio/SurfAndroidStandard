package ru.surfstudio.core_mvp_rxbinding.base.ui.swr

import androidx.annotation.CallSuper
import ru.surfstudio.android.core.mvp.view.CoreView

abstract class BaseSwrRxActivityView<M : SwrRxModel>
    : CoreView, SwrRxView<M> {
    @CallSuper
    override fun bind(sm: M) {
        bindSwipeRefreshState(sm)
    }
}