package ru.surfstudio.standard.base_ui.loadstate.presentation

import android.view.LayoutInflater
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import ru.surfstudio.standard.base_ui.loadstate.PlaceHolderViewContainer
import ru.surfstudio.android.core.mvp.loadstate.SimpleLoadStatePresentation
import ru.surfstudio.standard.base_ui.loadstate.state.TransparentLoadingState
import ru.surfstudio.android.template.base_ui.R
import ru.surfstudio.standard.base_ui.loadstate.setClickableAndFocusable

/**
 * Представление состояния TransparentLoading в виде ProgressBar поверх затемненного фона
 */
class TransparentLoadingStatePresentation(
        private val placeHolder: PlaceHolderViewContainer
) : SimpleLoadStatePresentation<TransparentLoadingState>() {

    @ColorInt
    var transparentBackgroundColor = ContextCompat.getColor(placeHolder.context, R.color.transparent_bg_color)

    val view: View by lazy {
        LayoutInflater.from(placeHolder.context).inflate(
                R.layout.layout_load_state,
                placeHolder,
                false)
    }

    override fun showState(state: TransparentLoadingState) {
        with(placeHolder) {
            setBackgroundColor(transparentBackgroundColor)
            changeViewTo(view)
            setClickableAndFocusable(true)
            show()
        }
    }
}