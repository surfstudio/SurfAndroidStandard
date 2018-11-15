package ru.surfstudio.android.sample.common.ui.base.loadstate.presentations

import android.support.annotation.ColorInt
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import ru.surfstudio.android.core.mvp.loadstate.renderer.LoadStatePresentation
import ru.surfstudio.android.core.mvp.model.state.LoadStateInterface
import ru.surfstudio.android.sample.common.R
import ru.surfstudio.android.sample.common.ui.base.loadstate.TransparentLoadingState
import ru.surfstudio.android.sample.common.ui.base.loadstate.renderer.PlaceHolderViewContainer

class TransparentLoadingStatePresentation(private val placeHolder: PlaceHolderViewContainer)
    : LoadStatePresentation<TransparentLoadingState> {

    @ColorInt
    var transparentBackgroundColor = ContextCompat.getColor(
            placeHolder.context,
            R.color.colorTransparentBackground)

    val view: View by lazy {
        with(LayoutInflater.from(placeHolder.context).inflate(
                R.layout.placeholder_view_loading_strategy,
                placeHolder,
                false)) {
            setBackgroundColor(transparentBackgroundColor)
            return@lazy this
        }
    }

    override fun showPresentation(
            loadStateFrom: LoadStateInterface,
            loadStateTo: TransparentLoadingState) {
        placeHolder.changeVisibility(View.VISIBLE)
        placeHolder.changeViewTo(view)
    }

    override fun hidePresentation(
            loadStateFrom: TransparentLoadingState,
            loadStateTo: LoadStateInterface) {
    }
}