package ru.surfstudio.core_mvp_rxbinding_sample

import ru.surfstudio.core_mvp_rxbinding.base.domain.Action
import ru.surfstudio.core_mvp_rxbinding.base.domain.State
import ru.surfstudio.core_mvp_rxbinding.base.ui.RxModel
import ru.surfstudio.core_mvp_rxbinding.base.ui.lds.LdsRxModel

class MainModel: LdsRxModel() {
    val textViewStatus = State<Int>(0)
    val incAction = Action<Unit>()
    val decAction = Action<Unit>()

}