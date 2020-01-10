package ru.surfstudio.android.easyadapter.sample.ui.screen.multitype

import ru.surfstudio.android.core.mvp.model.ScreenModel
import ru.surfstudio.android.easyadapter.sample.domain.FirstData
import ru.surfstudio.android.easyadapter.sample.domain.SecondData

class MultitypeListScreenModel : ScreenModel() {
    val firstData = FirstData(10)
    val secondData = SecondData("Second data")

    val firstDataList = arrayListOf(
            FirstData(0),
            FirstData(1),
            FirstData(2)
    )

    val secondDataList = arrayListOf(
            SecondData("a"),
            SecondData("b"),
            SecondData("c")
    )
}