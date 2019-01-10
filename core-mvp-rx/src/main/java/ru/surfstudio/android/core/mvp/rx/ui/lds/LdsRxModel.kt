package ru.surfstudio.android.core.mvp.rx.ui.lds

import ru.surfstudio.android.core.mvp.loadstate.LoadStateInterface
import ru.surfstudio.android.core.mvp.rx.domain.State
import ru.surfstudio.android.core.mvp.rx.ui.RxModel

open class LdsRxModel: RxModel() {
    val loadState = State<LoadStateInterface>()
}