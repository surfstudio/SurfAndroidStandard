package ru.surfstudio.android.core.mvp.rx.ui.swr_lds

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import io.reactivex.functions.Consumer
import ru.surfstudio.android.core.mvp.model.state.SwipeRefreshState
import ru.surfstudio.android.core.mvp.rx.ui.lds.LdsRxView

interface LdsSwrBindableView<M : LdsSwrRxModel> : LdsRxView<M> {
    fun getSwipeRefreshLayout(): SwipeRefreshLayout


    fun bindSwipeRefreshState(sm: M) {
        sm.swipeRefreshState.observable.bindTo(getSwipeRefreshLayout().refreshConsumer)
    }

    private val SwipeRefreshLayout.refreshConsumer get() = Consumer<SwipeRefreshState> {
        isRefreshing = it == SwipeRefreshState.REFRESHING
    }
}