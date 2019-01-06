package ru.surfstudio.core_mvp_rxbinding.base.ui.swr

import ru.surfstudio.android.core.mvp.model.ScreenModel
import ru.surfstudio.android.core.mvp.model.state.SwipeRefreshState
import ru.surfstudio.core_mvp_rxbinding.base.domain.State
import ru.surfstudio.core_mvp_rxbinding.base.ui.RxModel

open class SwrRxModel : RxModel() {
    val swipeRefreshState = State(SwipeRefreshState.HIDE)
}