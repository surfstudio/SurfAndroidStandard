package ru.surfstudio.standard.base_ui.loadstate.presentation

import ru.surfstudio.standard.base_ui.loadstate.PlaceHolderViewContainer
import ru.surfstudio.android.core.mvp.loadstate.SimpleLoadStatePresentation
import ru.surfstudio.standard.base_ui.loadstate.state.NoneLoadState

/**
 * Представление состояния NoneLoadState, скрывающее PlaceHolderViewContainer
 */
class NoneLoadStatePresentation(
        private val placeHolder: PlaceHolderViewContainer
) : SimpleLoadStatePresentation<NoneLoadState>() {

    override fun showState(state: NoneLoadState) {
        placeHolder.hide()
        placeHolder.removeAllViews()
    }
}