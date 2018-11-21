package ru.surfstudio.android.sample.common.ui.base.loadstate.presentations

import android.view.View
import ru.surfstudio.android.core.mvp.loadstate.SimpleLoadStatePresentation
import ru.surfstudio.android.sample.common.ui.base.loadstate.NoneLoadState
import ru.surfstudio.android.sample.common.ui.base.loadstate.renderer.PlaceHolderViewContainer

/**
 * Представление состояния NoneLoadState, скрывающее PlaceHolderViewContainer
 */
class NoneLoadStatePresentation(private val placeHolder: PlaceHolderViewContainer) :
        SimpleLoadStatePresentation<NoneLoadState>() {

    override fun showState(state: NoneLoadState) {
        placeHolder.changeVisibility(View.INVISIBLE)
        placeHolder.changeViewTo()
    }

}