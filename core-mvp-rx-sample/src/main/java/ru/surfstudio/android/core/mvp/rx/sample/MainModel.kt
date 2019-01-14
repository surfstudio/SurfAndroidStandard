package ru.surfstudio.android.core.mvp.rx.sample

import ru.surfstudio.android.core.mvp.rx.domain.Action
import ru.surfstudio.android.core.mvp.rx.domain.State
import ru.surfstudio.android.core.mvp.rx.domain.TwoWay
import ru.surfstudio.android.core.mvp.rx.ui.lds.LdsRxModel

class MainModel : LdsRxModel() {
    val counterState = TwoWay<Int>()
    val incAction = Action<Unit>()
    val decAction = Action<Unit>()

    val textEditState = TwoWay<String>()
    val doubleTextAction = Action<Unit>()
    val sampleCommand = State<String>()
}