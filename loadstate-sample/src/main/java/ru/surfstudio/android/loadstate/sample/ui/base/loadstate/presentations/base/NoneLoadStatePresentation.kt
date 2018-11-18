package ru.surfstudio.android.loadstate.sample.ui.base.loadstate.presentations.base

import android.view.View
import ru.surfstudio.android.core.mvp.loadstate.renderer.LoadStatePresentation
import ru.surfstudio.android.core.mvp.model.state.LoadStateInterface
import ru.surfstudio.android.loadstate.sample.ui.base.loadstate.renderer.PlaceHolderViewContainer
import ru.surfstudio.android.loadstate.sample.ui.base.loadstate.states.NoneLoadState

/**
 * Представление состояния NoneLoadState, скрывающее PlaceHolderViewContainer
 */
class NoneLoadStatePresentation(private val placeHolder: PlaceHolderViewContainer? = null) : LoadStatePresentation<NoneLoadState> {

    override fun showPresentation(loadStateFrom: LoadStateInterface, loadStateTo: NoneLoadState) {
        placeHolder?.let {
            it.changeVisibility(View.INVISIBLE)
            it.changeViewTo()
        }
    }

    override fun hidePresentation(loadStateFrom: NoneLoadState, loadStateTo: LoadStateInterface) {
    }
}