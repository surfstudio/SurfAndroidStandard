package ru.surfstudio.android.core.mvp.rx.ui.swr_lds

import ru.surfstudio.android.core.mvp.loadstate.LoadStateInterface
import ru.surfstudio.android.core.mvp.model.state.SwipeRefreshState
import ru.surfstudio.android.core.mvp.rx.domain.State
import ru.surfstudio.android.core.mvp.rx.ui.RxModel
import ru.surfstudio.android.core.mvp.rx.ui.lds.HasLoadState
import ru.surfstudio.android.core.mvp.rx.ui.lds.LdsRxModel
import ru.surfstudio.android.core.mvp.rx.ui.swr.HasSwrState

open class LdsSwrRxModel: RxModel(), HasLoadState, HasSwrState {
    override val loadState = State<LoadStateInterface>()
    override val swipeRefreshState = State(SwipeRefreshState.HIDE)
}