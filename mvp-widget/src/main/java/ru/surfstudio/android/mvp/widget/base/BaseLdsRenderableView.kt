package ru.surfstudio.android.mvp.widget.base

import ru.surfstudio.android.core.mvp.loadstate.LoadStateRendererInterface
import ru.surfstudio.android.core.mvp.model.LdsScreenModel
import ru.surfstudio.android.core.mvp.loadstate.LoadStateInterface
import ru.surfstudio.android.core.mvp.view.CoreView

/**
 * Базовый интерфейс для виджетов, сохраняющих [LoadState]
 */
interface BaseLdsRenderableView<M : LdsScreenModel> : BaseRenderableView<M>, CoreView {

    val loadStateRenderer: LoadStateRendererInterface

    override fun render(screenModel: M) {
        this.renderLoadState(screenModel.loadState)
        this.renderInternal(screenModel)
    }

    fun renderLoadState(loadState: LoadStateInterface) {
        loadStateRenderer.render(loadState)
    }
}
