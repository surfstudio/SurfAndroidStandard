package ru.surfstudio.android.core.mvp.rx.ui.swr_lds

import ru.surfstudio.android.core.mvp.model.state.SwipeRefreshState
import ru.surfstudio.android.core.mvp.rx.domain.State
import ru.surfstudio.android.core.mvp.rx.ui.lds.LdsRxModel

open class LdsSwrRxModel: LdsRxModel() {
    val swipeRefreshState = State(SwipeRefreshState.HIDE)
}