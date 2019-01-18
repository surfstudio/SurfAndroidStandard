package ru.surfstudio.android.core.mvp.rx.sample

import ru.surfstudio.android.utilktx.ktx.text.EMPTY_STRING
import ru.surfstudio.android.core.mvp.rx.domain.Action
import ru.surfstudio.android.core.mvp.rx.domain.TextStateManager
import ru.surfstudio.android.core.mvp.rx.domain.State
import ru.surfstudio.android.core.mvp.rx.ui.lds.LdsRxModel

class MainModel : LdsRxModel() {
    val counterState = State<Int>(0)
    val incAction = Action<Unit>()
    val decAction = Action<Unit>()
    val longQueryAction = Action<Unit>()

    val textEditState = TextStateManager(EMPTY_STRING)
    val doubleTextAction = Action<Unit>()

}