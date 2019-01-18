package ru.surfstudio.android.mvpwidget.sample.ui.screen.main.list

import ru.surfstudio.android.core.mvp.model.ScreenModel

/**
 * Модель экрана [ListActivityView]
 */
class ListScreenModel : ScreenModel() {
    val itemList: List<Int> = (0..100).toList()
}
