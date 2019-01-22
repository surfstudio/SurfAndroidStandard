package ru.surfstudio.android.core.mvp.rx.ui.lds

import ru.surfstudio.android.core.mvp.loadstate.LoadStateInterface
import ru.surfstudio.android.core.mvp.rx.domain.State

interface HasLoadState {
    val loadState: State<LoadStateInterface>
}