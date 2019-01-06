package ru.surfstudio.core_mvp_rxbinding.base.ui.swr

import androidx.annotation.CallSuper
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import io.reactivex.functions.Consumer
import ru.surfstudio.android.core.mvp.model.state.SwipeRefreshState
import ru.surfstudio.core_mvp_rxbinding.base.ui.BindableRxView

interface SwrRxView<M : SwrRxModel> : BindableRxView<M> {
    fun getSwipeRefreshLayout(): SwipeRefreshLayout

    @CallSuper
    override fun bind(sm: M) {
        bindSwipeRefreshState(sm)
    }

    fun bindSwipeRefreshState(sm: M) {
        sm.swipeRefreshState.observable.bindTo(getSwipeRefreshLayout().refreshConsumer)
    }

    private val SwipeRefreshLayout.refreshConsumer
        get() = Consumer<SwipeRefreshState> { state ->
            isRefreshing = state == SwipeRefreshState.REFRESHING
        }
}