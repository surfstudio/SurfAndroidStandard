package ru.surfstudio.android.loadstate.sample.ui.base.loadstate.presentations

import android.view.LayoutInflater
import android.view.View
import ru.surfstudio.android.core.mvp.loadstate.renderer.LoadStatePresentation
import ru.surfstudio.android.core.mvp.model.state.LoadStateInterface
import ru.surfstudio.android.loadstate.sample.R
import ru.surfstudio.android.loadstate.sample.ui.base.loadstate.renderer.PlaceHolderViewContainer
import ru.surfstudio.android.loadstate.sample.ui.base.loadstate.states.CustomLoadState

class CustomLoadStatePresentation(private val placeHolder: PlaceHolderViewContainer) :
        LoadStatePresentation<CustomLoadState> {

    private val view: View by lazy {
        with(LayoutInflater.from(placeHolder.context).inflate(
                R.layout.custom_load_state_presentation,
                placeHolder,
                false)) {
            return@lazy this
        }
    }

    override fun showPresentation(loadStateFrom: LoadStateInterface, loadStateTo: CustomLoadState) {
        placeHolder.changeViewTo(view)
        placeHolder.changeVisibility(View.VISIBLE)
    }

    override fun hidePresentation(loadStateFrom: CustomLoadState, loadStateTo: LoadStateInterface) {

    }
}