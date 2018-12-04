package ru.surfstudio.standard.base_ui.loadstate.presentation

import android.view.LayoutInflater
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import ru.surfstudio.standard.base_ui.loadstate.PlaceHolderViewContainer
import ru.surfstudio.android.core.mvp.loadstate.SimpleLoadStatePresentation
import ru.surfstudio.standard.base_ui.loadstate.state.MainLoadingState
import ru.surfstudio.android.core.mvp.loadstate.LoadStateInterface
import ru.surfstudio.android.template.base_ui.R
import ru.surfstudio.standard.base_ui.loadstate.utils.clickAndFocus

/**
 * Представление состояния MainLoading в виде ProgressBar
 */
class MainLoadingStatePresentation(
        private val placeHolder: PlaceHolderViewContainer
) : SimpleLoadStatePresentation<MainLoadingState>() {

    @ColorInt
    private val backgroundColor = ContextCompat.getColor(placeHolder.context, R.color.colorWindowBackgroundDark)

    private val view: View by lazy {
        LayoutInflater.from(placeHolder.context)
                .inflate(R.layout.load_state_main, placeHolder, false)
    }

    override fun showState(state: MainLoadingState) {
        with(placeHolder) {
            removeAllViews()
            addView(view)
            setBackgroundColor(backgroundColor)
            clickAndFocus(true)
            show()
        }
    }

    override fun hideState(state: MainLoadingState, nextState: LoadStateInterface) {
        with(placeHolder) {
            setBackgroundColor(ContextCompat.getColor(placeHolder.context, android.R.color.transparent))
            clickAndFocus(false)
        }
    }
}