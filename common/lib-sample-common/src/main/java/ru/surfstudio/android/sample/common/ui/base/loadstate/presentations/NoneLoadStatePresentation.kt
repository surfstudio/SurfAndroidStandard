package ru.surfstudio.android.sample.common.ui.base.loadstate.presentations

import ru.surfstudio.android.core.mvp.loadstate.SimpleLoadStatePresentation
import ru.surfstudio.android.custom.view.placeholder.PlaceHolderViewContainer
import ru.surfstudio.android.sample.common.ui.base.loadstate.NoneLoadState

/**
 * NoneLoadState presentation which hides PlaceHolderViewContainer
 */
class NoneLoadStatePresentation(private val placeHolder: PlaceHolderViewContainer) :
        SimpleLoadStatePresentation<NoneLoadState>() {

    override fun showState(state: NoneLoadState) {
        placeHolder.hide()
        placeHolder.removeAllViews()
    }

}