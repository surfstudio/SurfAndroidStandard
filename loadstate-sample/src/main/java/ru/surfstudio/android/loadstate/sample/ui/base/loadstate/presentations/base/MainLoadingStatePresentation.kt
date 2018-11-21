package ru.surfstudio.android.loadstate.sample.ui.base.loadstate.presentations.base

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import me.zhanghai.android.materialprogressbar.MaterialProgressBar
import ru.surfstudio.android.core.mvp.loadstate.SimpleLoadStatePresentation
import ru.surfstudio.android.loadstate.sample.R
import ru.surfstudio.android.loadstate.sample.ui.base.loadstate.renderer.PlaceHolderViewContainer
import ru.surfstudio.android.loadstate.sample.ui.base.loadstate.states.MainLoadingState

/**
 * Представление состояния MainLoading в виде ProgressBar
 */
class MainLoadingStatePresentation(private val placeHolder: PlaceHolderViewContainer) :
        SimpleLoadStatePresentation<MainLoadingState>() {

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

    override fun showState(state: MainLoadingState) {
        placeHolder.changeVisibility(View.VISIBLE)
        placeHolder.removeAllViews()
        placeHolder.addView(view)
    }

    private fun colorProgressBar(view: View) {
        val progressBar: MaterialProgressBar = view.findViewById(R.id.placeholder_loading_pb)
        progressBar.indeterminateTintList = ColorStateList.valueOf(progressBarColor)
    }
}