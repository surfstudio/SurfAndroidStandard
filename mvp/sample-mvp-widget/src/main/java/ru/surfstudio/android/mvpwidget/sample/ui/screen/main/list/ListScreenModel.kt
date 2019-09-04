package ru.surfstudio.android.mvpwidget.sample.ui.screen.main.list

import ru.surfstudio.android.core.mvp.model.ScreenModel

/**
 * Модель экрана [ListActivityView]
 */
class ListScreenModel : ScreenModel() {
    var itemList: List<ListItem> = createNewItemList()

    fun createNewItemList() = (0..100).map { ListItem(it) }
}