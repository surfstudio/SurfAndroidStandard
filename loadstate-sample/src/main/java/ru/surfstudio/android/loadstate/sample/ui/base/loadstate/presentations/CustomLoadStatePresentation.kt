package ru.surfstudio.android.loadstate.sample.ui.base.loadstate.presentations

import android.view.LayoutInflater
import android.view.View
import ru.surfstudio.android.core.mvp.loadstate.LoadStateInterface
import ru.surfstudio.android.core.mvp.loadstate.LoadStatePresentation
import ru.surfstudio.android.custom.view.placeholder.PlaceHolderViewContainer
import ru.surfstudio.android.loadstate.sample.R
import ru.surfstudio.android.loadstate.sample.ui.base.loadstate.states.CustomLoadState

/**
 * Пример представления какого-то кастомного состояния
 */
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

    override fun showState(state: CustomLoadState, previousState: LoadStateInterface) {
        placeHolder.changeViewTo(view)
        placeHolder.show()
    }

    override fun hideState(state: CustomLoadState, nextState: LoadStateInterface) {

    }
}