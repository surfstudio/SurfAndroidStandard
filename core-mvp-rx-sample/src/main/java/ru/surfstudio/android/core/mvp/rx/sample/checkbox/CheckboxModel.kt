package ru.surfstudio.android.core.mvp.rx.sample.checkbox

import ru.surfstudio.android.core.mvp.rx.domain.Action
import ru.surfstudio.android.core.mvp.rx.domain.State
import ru.surfstudio.android.core.mvp.rx.ui.lds.LdsRxModel

class CheckboxModel : LdsRxModel() {

    val checkAction1 = Action<Boolean>()
    val checkAction2 = Action<Boolean>()
    val checkAction3 = Action<Boolean>()

    val count = State<Int>()
}