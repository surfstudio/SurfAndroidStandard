package ru.surfstudio.standard.ui.placeholder.loadstate.presentation

import android.view.View
import ru.surfstudio.android.core.mvp.loadstate.SimpleLoadStatePresentation
import ru.surfstudio.android.custom.view.placeholder.PlaceHolderViewContainer
import ru.surfstudio.standard.ui.placeholder.loadstate.state.NoneState

/**
 * Представление состояния NoneState, скрывающее PlaceHolderViewContainer
 */
class NoneLoadStatePresentation(
        private val placeHolder: PlaceHolderViewContainer
) : SimpleLoadStatePresentation<NoneState>() {

    override fun showState(state: NoneState) {
        with(placeHolder) {
            changeViewTo(View(context))
            hide()
        }
    }
}