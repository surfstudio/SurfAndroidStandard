package ru.surfstudio.core_mvp_rxbinding.base.ui.lds

import ru.surfstudio.android.core.mvp.loadstate.LoadStateInterface
import ru.surfstudio.android.core.mvp.model.ScreenModel
import ru.surfstudio.core_mvp_rxbinding.base.domain.State
import ru.surfstudio.core_mvp_rxbinding.base.ui.RxModel

open class LdsRxModel: RxModel() {
    val loadState = State<LoadStateInterface>()
}