package ru.surfstudio.android.loadstate.sample.ui.screen.ordinary

import ru.surfstudio.android.core.mvp.model.LdsScreenModel

/**
 * Модель экрана для демонстрации работы DefaultLoadStateRenderer
 */
class DefaultRendererDemoScreenModel : LdsScreenModel() {
    var itemList: List<Int> = listOf()
}
