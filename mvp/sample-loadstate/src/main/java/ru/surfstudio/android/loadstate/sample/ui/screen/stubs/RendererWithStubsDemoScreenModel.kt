package ru.surfstudio.android.loadstate.sample.ui.screen.stubs

import ru.surfstudio.android.core.mvp.model.LdsScreenModel

/**
 * Модель экрана для демонстрации работы DefaultLoadStateRenderer с использованием заглушек (шиммеров)
 */
class RendererWithStubsDemoScreenModel : LdsScreenModel() {
    var itemList: List<Int> = listOf()
}
