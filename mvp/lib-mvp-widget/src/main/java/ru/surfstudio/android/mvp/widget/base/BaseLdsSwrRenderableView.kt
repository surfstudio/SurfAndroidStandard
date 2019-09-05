package ru.surfstudio.android.mvp.widget.base

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ru.surfstudio.android.core.mvp.model.LdsSwrScreenModel
import ru.surfstudio.android.core.mvp.model.state.SwipeRefreshState
import ru.surfstudio.android.core.mvp.view.CoreView

/**
 * Базовый интерфейс для виджетов с поддержкой
 * состояния загрузки [LoadState]
 * состояния SwipeRefresh [SwipeRefreshState]
 */
interface BaseLdsSwrRenderableView<M : LdsSwrScreenModel> : BaseLdsRenderableView<M>, CoreView {

    fun getSwipeRefreshLayout(): SwipeRefreshLayout

    override fun render(screenModel: M) {
        this.renderLoadState(screenModel.loadState)
        this.renderSwipeRefreshState(screenModel.swipeRefreshState)
        this.renderInternal(screenModel)
    }

    fun renderSwipeRefreshState(swipeRefreshState: SwipeRefreshState) {
        this.getSwipeRefreshLayout().isRefreshing = swipeRefreshState == SwipeRefreshState.REFRESHING
    }
}
