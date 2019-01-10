package ru.surfstudio.android.core.mvp.rx.ui.swr

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import io.reactivex.functions.Consumer
import ru.surfstudio.android.core.mvp.model.state.SwipeRefreshState
import ru.surfstudio.android.core.mvp.rx.ui.BindableRxView

interface SwrRxView<M : SwrRxModel> : BindableRxView<M> {
    fun getSwipeRefreshLayout(): SwipeRefreshLayout

    fun bindSwipeRefreshState(sm: M) {
        sm.swipeRefreshState.observable.bindTo(getSwipeRefreshLayout().refreshConsumer)
    }

    private val SwipeRefreshLayout.refreshConsumer
        get() = Consumer<SwipeRefreshState> { state ->
            isRefreshing = state == SwipeRefreshState.REFRESHING
        }
}