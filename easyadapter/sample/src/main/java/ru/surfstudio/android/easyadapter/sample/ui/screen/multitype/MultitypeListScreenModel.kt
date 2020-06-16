package ru.surfstudio.android.easyadapter.sample.ui.screen.multitype

import ru.surfstudio.android.core.mvp.model.ScreenModel
import ru.surfstudio.android.easyadapter.sample.domain.FirstData
import ru.surfstudio.android.easyadapter.sample.domain.SecondData

class MultitypeListScreenModel : ScreenModel() {
    val firstData = FirstData(10)
    val secondData = SecondData("Sed ut perspiciatis")

    val firstDataList = arrayListOf(
            FirstData(0),
            FirstData(1),
            FirstData(2)
    )

    val secondDataList = arrayListOf(
            SecondData("Lorem ipsum dolor sit amet"),
            SecondData("consectetur adipiscing elit"),
            SecondData("sed do eiusmod tempor incididunt")
    )
}