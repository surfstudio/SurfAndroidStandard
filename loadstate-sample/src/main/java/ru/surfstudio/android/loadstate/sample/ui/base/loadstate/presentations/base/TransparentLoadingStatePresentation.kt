package ru.surfstudio.android.loadstate.sample.ui.base.loadstate.presentations.base

import android.view.LayoutInflater
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import ru.surfstudio.android.core.mvp.loadstate.LoadStateInterface
import ru.surfstudio.android.core.mvp.loadstate.LoadStatePresentation
import ru.surfstudio.android.custom.view.placeholder.PlaceHolderViewContainer
import ru.surfstudio.android.loadstate.sample.R
import ru.surfstudio.android.loadstate.sample.ui.base.loadstate.states.TransparentLoadingState

/**
 * Представление состояния TransparentLoading в виде ProgressBar поверх затемненного фона
 */
class TransparentLoadingStatePresentation(private val placeHolder: PlaceHolderViewContainer)
    : LoadStatePresentation<TransparentLoadingState> {

    @ColorInt
    var transparentBackgroundColor = ContextCompat.getColor(
            placeHolder.context,
            R.color.colorTransparentBackground)

    val view: View by lazy {
        LayoutInflater.from(placeHolder.context).inflate(
                R.layout.loading_state_presentation,
                placeHolder,
                false)
    }

    override fun showState(
            state: TransparentLoadingState,
            previousState: LoadStateInterface) {
        placeHolder.setBackgroundColor(transparentBackgroundColor)
        placeHolder.changeViewTo(view)
        placeHolder.show()
    }

    override fun hideState(
            state: TransparentLoadingState,
            nextState: LoadStateInterface) {
        placeHolder.setBackgroundColor(
                ContextCompat.getColor(
                        placeHolder.context,
                        android.R.color.transparent))
    }
}