package ru.surfstudio.core_mvp_rxbinding_sample

import ru.surfstudio.android.utilktx.ktx.text.EMPTY_STRING
import ru.surfstudio.core_mvp_rxbinding.base.domain.Action
import ru.surfstudio.core_mvp_rxbinding.base.domain.TextStateManager
import ru.surfstudio.core_mvp_rxbinding.base.domain.State
import ru.surfstudio.core_mvp_rxbinding.base.ui.lds.LdsRxModel

class MainModel : LdsRxModel() {
    val counterState = State<Int>(0)
    val incAction = Action<Unit>()
    val decAction = Action<Unit>()

    val textEditState = TextStateManager(EMPTY_STRING)
    val doubleTextAction = Action<Unit>()

}