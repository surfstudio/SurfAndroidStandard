package ru.surfstudio.android.mvp.widget.base

import ru.surfstudio.android.core.mvp.model.LdsScreenModel
import ru.surfstudio.android.core.mvp.model.state.LoadState
import ru.surfstudio.android.core.mvp.placeholder.PlaceHolderViewInterface
import ru.surfstudio.android.core.mvp.view.CoreView

/**
 * Базовый интерфейс для виджетов, сохраняющих [LoadState]
 */
interface BaseLdsRenderableView<M : LdsScreenModel> : BaseRenderableView<M>, CoreView {

    fun getPlaceHolderView(): PlaceHolderViewInterface

    override fun render(screenModel: M) {
        this.renderLoadState(screenModel.loadState)
        this.renderInternal(screenModel)
    }

    fun renderLoadState(loadState: LoadState) {
        this.getPlaceHolderView().render(loadState)
    }
}
