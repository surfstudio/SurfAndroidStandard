package ru.surfstudio.android.mvp.widget.base

import ru.surfstudio.android.core.mvp.model.ScreenModel
import ru.surfstudio.android.core.mvp.view.CoreView
import ru.surfstudio.android.core.mvp.view.RenderableView

/**
 * Базовый интерфейс для виджетов с моделью экрана
 */
interface BaseRenderableView<M : ScreenModel> : RenderableView<M>, CoreView {

    fun renderInternal(screenModel: M)

    override fun render(screenModel: M) {
        this.renderInternal(screenModel)
    }
}