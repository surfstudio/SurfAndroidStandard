package ru.surfstudio.core_mvp_rxbinding.base.ui.swr_lds

import ru.surfstudio.android.core.mvp.loadstate.LoadStateInterface
import ru.surfstudio.android.core.mvp.model.ScreenModel
import ru.surfstudio.android.core.mvp.model.state.SwipeRefreshState
import ru.surfstudio.core_mvp_rxbinding.base.domain.State
import ru.surfstudio.core_mvp_rxbinding.base.ui.lds.LdsRxModel

open class LdsSwrRxModel: LdsRxModel() {
    val swipeRefreshState = State(SwipeRefreshState.HIDE)
}