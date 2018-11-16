package ru.surfstudio.android.loadstate.sample.ui.base.loadstate.presentations

import android.view.View
import ru.surfstudio.android.core.mvp.loadstate.renderer.LoadStatePresentation
import ru.surfstudio.android.core.mvp.model.state.LoadStateInterface
import ru.surfstudio.android.loadstate.sample.ui.base.loadstate.renderer.PlaceHolderViewContainer
import ru.surfstudio.android.loadstate.sample.ui.base.loadstate.states.NoneLoadState

class NoneLoadStatePresentation(private val placeHolder: PlaceHolderViewContainer) : LoadStatePresentation<NoneLoadState> {

    override fun showPresentation(loadStateFrom: LoadStateInterface, loadStateTo: NoneLoadState) {
        placeHolder.changeVisibility(View.INVISIBLE)
        placeHolder.changeViewTo()
    }

    override fun hidePresentation(loadStateFrom: NoneLoadState, loadStateTo: LoadStateInterface) {
    }
}