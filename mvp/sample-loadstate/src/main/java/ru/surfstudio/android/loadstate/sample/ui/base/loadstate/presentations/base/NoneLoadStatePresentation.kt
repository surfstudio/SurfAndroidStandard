package ru.surfstudio.android.loadstate.sample.ui.base.loadstate.presentations.base

import android.view.View
import ru.surfstudio.android.core.mvp.loadstate.SimpleLoadStatePresentation
import ru.surfstudio.android.custom.view.placeholder.PlaceHolderViewContainer
import ru.surfstudio.android.loadstate.sample.ui.base.loadstate.states.NoneLoadState

/**
 * Представление состояния NoneLoadState, скрывающее PlaceHolderViewContainer
 */
class NoneLoadStatePresentation(private val placeHolder: PlaceHolderViewContainer? = null) :
        SimpleLoadStatePresentation<NoneLoadState>() {

    override fun showState(state: NoneLoadState) {
        placeHolder?.run {
            hide()
            removeAllViews()
        }
    }
}