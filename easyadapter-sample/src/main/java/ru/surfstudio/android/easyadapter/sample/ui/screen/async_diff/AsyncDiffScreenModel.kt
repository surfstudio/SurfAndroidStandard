package ru.surfstudio.android.easyadapter.sample.ui.screen.async_diff

import ru.surfstudio.android.core.mvp.model.ScreenModel
import kotlin.random.Random

class AsyncDiffScreenModel(var data: List<Int> = emptyList()) : ScreenModel() {

    fun generateNewDataList() {
        data = (0..99).map { Random.nextInt(0, 100) }
    }
}