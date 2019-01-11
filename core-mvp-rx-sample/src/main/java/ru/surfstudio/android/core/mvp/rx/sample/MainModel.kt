package ru.surfstudio.android.core.mvp.rx.sample

import ru.surfstudio.android.core.mvp.rx.domain.Action
import ru.surfstudio.android.core.mvp.rx.domain.Command
import ru.surfstudio.android.core.mvp.rx.domain.State
import ru.surfstudio.android.core.mvp.rx.ui.lds.LdsRxModel

class MainModel : LdsRxModel() {
    val counterState = State<Int>()
    val incAction = Action<Unit>()
    val decAction = Action<Unit>()

    val textEditState = State<String>()
    val doubleTextAction = Action<Unit>()
    val sampleCommand = Command<Unit>()
}