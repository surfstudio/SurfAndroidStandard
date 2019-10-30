package ru.surfstudio.android.easyadapter.sample.ui.screen.async_diff

import ru.surfstudio.android.core.mvp.model.ScreenModel

class AsyncDiffScreenModel(var data: List<Int> = emptyList()) : ScreenModel() {

    fun generateNewDataList() {
        data = (0..99).toList().shuffled()
    }
}