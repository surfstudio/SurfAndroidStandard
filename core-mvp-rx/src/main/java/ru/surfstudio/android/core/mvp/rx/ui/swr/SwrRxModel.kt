package ru.surfstudio.android.core.mvp.rx.ui.swr

import ru.surfstudio.android.core.mvp.model.state.SwipeRefreshState
import ru.surfstudio.android.core.mvp.rx.domain.State
import ru.surfstudio.android.core.mvp.rx.ui.RxModel

open class SwrRxModel : RxModel() {
    val swipeRefreshState = State<SwipeRefreshState>()
}