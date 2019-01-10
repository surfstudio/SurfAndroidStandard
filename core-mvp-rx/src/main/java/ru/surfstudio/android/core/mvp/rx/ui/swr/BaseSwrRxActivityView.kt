package ru.surfstudio.android.core.mvp.rx.ui.swr

import androidx.annotation.CallSuper
import ru.surfstudio.android.core.mvp.view.CoreView

abstract class BaseSwrRxActivityView<M : SwrRxModel>
    : CoreView, SwrRxView<M> {
    @CallSuper
    override fun bind(sm: M) {
        bindSwipeRefreshState(sm)
    }
}