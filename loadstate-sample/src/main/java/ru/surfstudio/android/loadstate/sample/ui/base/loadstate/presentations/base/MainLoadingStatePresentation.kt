package ru.surfstudio.android.loadstate.sample.ui.base.loadstate.presentations.base

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import me.zhanghai.android.materialprogressbar.MaterialProgressBar
import ru.surfstudio.android.core.mvp.loadstate.renderer.LoadStatePresentation
import ru.surfstudio.android.core.mvp.model.state.LoadStateInterface
import ru.surfstudio.android.loadstate.sample.ui.base.loadstate.renderer.PlaceHolderViewContainer
import ru.surfstudio.android.loadstate.sample.ui.base.loadstate.states.MainLoadingState
import ru.surfstudio.android.loadstate.sample.R

/**
 * Представление состояния MainLoading в виде ProgressBar
 */
class MainLoadingStatePresentation(private val placeHolder: PlaceHolderViewContainer) : LoadStatePresentation<MainLoadingState> {

    @ColorInt
    private val progressBarColor: Int = ContextCompat.getColor(placeHolder.context, R.color.colorAccent)

    private val view: View by lazy {
        with(LayoutInflater.from(placeHolder.context).inflate(
                R.layout.loading_state_presentation,
                placeHolder,
                false)) {
            colorProgressBar(this)
            return@lazy this
        }
    }

    override fun showLoadState(loadStateFrom: LoadStateInterface, loadStateTo: MainLoadingState) {
        placeHolder.changeVisibility(View.VISIBLE)
        placeHolder.removeAllViews()
        placeHolder.addView(view)
    }

    override fun hidePresentation(loadStateFrom: MainLoadingState, loadStateTo: LoadStateInterface) {
    }

    private fun colorProgressBar(view: View) {
        val progressBar: MaterialProgressBar = view.findViewById(R.id.placeholder_loading_pb)
        progressBar.indeterminateTintList = ColorStateList.valueOf(progressBarColor)
    }
}