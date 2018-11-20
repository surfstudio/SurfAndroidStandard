package ru.surfstudio.android.sample.common.ui.base.loadstate.presentations

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import me.zhanghai.android.materialprogressbar.MaterialProgressBar
import ru.surfstudio.android.core.mvp.loadstate.SimpleLoadStatePresentation
import ru.surfstudio.android.sample.common.R
import ru.surfstudio.android.sample.common.ui.base.loadstate.MainLoadingState
import ru.surfstudio.android.sample.common.ui.base.loadstate.renderer.PlaceHolderViewContainer

/**
 * Представление состояния MainLoading в виде ProgressBar
 */
class MainLoadingLoadStatePresentation(private val placeHolder: PlaceHolderViewContainer) :
        SimpleLoadStatePresentation<MainLoadingState>() {

    @ColorInt
    private val progressBarColor: Int = ContextCompat.getColor(placeHolder.context, R.color.colorAccent)

    private val view: View by lazy {
        with(LayoutInflater.from(placeHolder.context).inflate(
                R.layout.placeholder_view_loading_strategy,
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