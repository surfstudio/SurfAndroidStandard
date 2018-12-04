package ru.surfstudio.standard.base_ui.loadstate.presentation

import android.view.LayoutInflater
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import ru.surfstudio.standard.base_ui.loadstate.PlaceHolderViewContainer
import ru.surfstudio.android.core.mvp.loadstate.LoadStateInterface
import ru.surfstudio.android.core.mvp.loadstate.LoadStatePresentation
import ru.surfstudio.standard.base_ui.loadstate.state.TransparentLoadingState
import ru.surfstudio.android.template.base_ui.R
import ru.surfstudio.standard.base_ui.loadstate.utils.clickAndFocus

/**
 * Представление состояния TransparentLoading в виде ProgressBar поверх затемненного фона
 */
class TransparentLoadingStatePresentation(
        private val placeHolder: PlaceHolderViewContainer
) : LoadStatePresentation<TransparentLoadingState> {

    @ColorInt
    var transparentBackgroundColor = ContextCompat.getColor(placeHolder.context, R.color.transparent_bg_color)

    val view: View by lazy {
        LayoutInflater.from(placeHolder.context).inflate(
                R.layout.load_state_transperant,
                placeHolder,
                false)
    }

    override fun showState(state: TransparentLoadingState, previousState: LoadStateInterface) {
        with(placeHolder) {
            setBackgroundColor(transparentBackgroundColor)
            changeViewTo(view)
            clickAndFocus(true)
            show()
        }
    }

    override fun hideState(state: TransparentLoadingState, nextState: LoadStateInterface) {
        with(placeHolder) {
            setBackgroundColor(ContextCompat.getColor(placeHolder.context, android.R.color.transparent))
            clickAndFocus(false)
        }
    }
}