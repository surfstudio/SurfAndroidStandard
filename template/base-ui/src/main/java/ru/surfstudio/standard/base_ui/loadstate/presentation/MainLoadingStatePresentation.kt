package ru.surfstudio.standard.base_ui.loadstate.presentation

import android.view.LayoutInflater
import android.view.View
import ru.surfstudio.android.core.mvp.loadstate.SimpleLoadStatePresentation
import ru.surfstudio.standard.base_ui.loadstate.PlaceHolderViewContainer
import ru.surfstudio.standard.base_ui.loadstate.state.MainLoadingState
import ru.surfstudio.android.template.base_ui.R
import ru.surfstudio.standard.base_ui.loadstate.setClickableAndFocusable

/**
 * Представление состояния MainLoading в виде ProgressBar
 */
class MainLoadingStatePresentation(
        private val placeHolder: PlaceHolderViewContainer
) : SimpleLoadStatePresentation<MainLoadingState>() {

    private val view: View by lazy {
        LayoutInflater.from(placeHolder.context).inflate(R.layout.layout_load_state, placeHolder, false)
    }

    override fun showState(state: MainLoadingState) {
        with(placeHolder) {
            changeViewTo(view)
            setClickableAndFocusable(true)
            show()
        }
    }
}