package ru.surfstudio.android.sample.common.ui.base.loadstate.presentations

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import me.zhanghai.android.materialprogressbar.MaterialProgressBar
import ru.surfstudio.android.core.mvp.loadstate.SimpleLoadStatePresentation
import ru.surfstudio.android.custom.view.placeholder.PlaceHolderViewContainer
import ru.surfstudio.android.sample.common.R
import ru.surfstudio.android.sample.common.ui.base.loadstate.MainLoadingState

/**
 * MainLoadingState presentation with a progressbar
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
        placeHolder.changeViewTo(view)
        placeHolder.show()
    }

    private fun colorProgressBar(view: View) {
        val progressBar: MaterialProgressBar = view.findViewById(R.id.placeholder_loading_pb)
        progressBar.supportIndeterminateTintList = ColorStateList.valueOf(progressBarColor)
    }
}