package ru.surfstudio.android.core.mvp.rx.sample

import com.jakewharton.rxrelay2.BehaviorRelay
import ru.surfstudio.android.utilktx.ktx.text.EMPTY_STRING
import ru.surfstudio.android.core.mvp.rx.domain.Action
import ru.surfstudio.android.core.mvp.rx.domain.Command
import ru.surfstudio.android.core.mvp.rx.domain.State
import ru.surfstudio.android.core.mvp.rx.domain.StateB
import ru.surfstudio.android.core.mvp.rx.ui.lds.LdsRxModel

class MainModel : LdsRxModel() {
    val counterState = StateB<Int>()
    val incAction = Action<Unit>()
    val decAction = Action<Unit>()

    val textEditState = StateB<String>()
    val doubleTextAction = Action<Unit>()
    val sampleCommand = Command<Unit>()
}