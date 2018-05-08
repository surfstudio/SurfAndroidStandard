package ru.surfstudio.android.mvp.binding.sample.ui.screen.main

import ru.surfstudio.android.core.mvp.binding.BindData
import ru.surfstudio.android.core.mvp.model.ScreenModel

class MainScreenModel : ScreenModel() {

    val panel1 = BindData(PaneDataModel(0, State.PRESSED))
    val panel2 = BindData(PaneDataModel())
    val panel3 = BindData(PaneDataModel())
    val panel4 = BindData(PaneDataModel())
    val panel5 = BindData(PaneDataModel())
    val panel6 = BindData(PaneDataModel())
    val panel7 = BindData(PaneDataModel())
    val panel8 = BindData(PaneDataModel())
    val panel9 = BindData(PaneDataModel())

}

data class PaneDataModel(val value: Int = 0, val state: State = State.UNPRESSED)

enum class State {
    PRESSED, UNPRESSED;

    fun next() =
            when (this) {
                State.PRESSED -> State.UNPRESSED
                State.UNPRESSED -> State.PRESSED
            }
}