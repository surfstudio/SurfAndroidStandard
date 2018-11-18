package ru.surfstudio.android.loadstate.sample.ui.base.loadstate.presentations.base

import android.view.LayoutInflater
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import ru.surfstudio.android.core.mvp.loadstate.renderer.LoadStatePresentation
import ru.surfstudio.android.core.mvp.model.state.LoadStateInterface
import ru.surfstudio.android.loadstate.sample.R
import ru.surfstudio.android.loadstate.sample.ui.base.loadstate.renderer.PlaceHolderViewContainer
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

    override fun showPresentation(
            loadStateFrom: LoadStateInterface,
            loadStateTo: TransparentLoadingState) {
        placeHolder.setBackgroundColor(transparentBackgroundColor)
        placeHolder.changeVisibility(View.VISIBLE)
        placeHolder.changeViewTo(view)
    }

    override fun hidePresentation(
            loadStateFrom: TransparentLoadingState,
            loadStateTo: LoadStateInterface) {
        placeHolder.setBackgroundColor(
                ContextCompat.getColor(
                        placeHolder.context,
                        android.R.color.transparent))
    }
}