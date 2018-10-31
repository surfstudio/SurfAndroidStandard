package ru.surfstudio.android.mvpwidget.sample.interactor.ui.screen.list

import ru.surfstudio.android.core.mvp.model.ScreenModel

/**
 * Модель экрана todo
 */
class ListScreenModel : ScreenModel() {
    val itemList: List<Int> = (0..100).toList()
}
