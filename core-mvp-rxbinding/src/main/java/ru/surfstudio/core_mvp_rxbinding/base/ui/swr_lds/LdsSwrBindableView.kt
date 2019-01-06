package ru.surfstudio.core_mvp_rxbinding.base.ui.swr_lds

import androidx.annotation.CallSuper
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import io.reactivex.functions.Consumer
import ru.surfstudio.android.core.mvp.model.state.SwipeRefreshState
import ru.surfstudio.core_mvp_rxbinding.base.ui.lds.LdsRxView

interface LdsSwrBindableView<M : LdsSwrRxModel> : LdsRxView<M> {
    fun getSwipeRefreshLayout(): SwipeRefreshLayout


    @CallSuper
    override fun bind(sm: M) {
        super.bind(sm)
        bindSwipeRefreshState(sm)
    }

    fun bindSwipeRefreshState(sm: M) {
        sm.swipeRefreshState.observable.bindTo(getSwipeRefreshLayout().refreshConsumer)
    }

    private val SwipeRefreshLayout.refreshConsumer get() = Consumer<SwipeRefreshState> {
        isRefreshing = it == SwipeRefreshState.REFRESHING
    }
}