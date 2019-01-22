package ru.surfstudio.android.core.mvp.rx.ui.swr

import ru.surfstudio.android.core.mvp.model.state.SwipeRefreshState
import ru.surfstudio.android.core.mvp.rx.domain.State

interface HasSwrState {
    val swipeRefreshState: State<SwipeRefreshState>
}