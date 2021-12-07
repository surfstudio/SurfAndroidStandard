package ru.surfstudio.android.loadstate.sample.ui.base.loadstate.presentations.base

import android.content.res.ColorStateList
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.widget.ProgressBar
import androidx.annotation.ColorInt
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import ru.surfstudio.android.core.mvp.loadstate.SimpleLoadStatePresentation
import ru.surfstudio.android.custom.view.placeholder.PlaceHolderViewContainer
import ru.surfstudio.android.loadstate.sample.R
import ru.surfstudio.android.loadstate.sample.ui.base.loadstate.states.MainLoadingState

/**
 * Представление состояния MainLoading в виде ProgressBar
 */
class MainLoadingStatePresentation(private val placeHolder: PlaceHolderViewContainer) :
    SimpleLoadStatePresentation<MainLoadingState>() {

    @ColorInt
    private val progressBarColor: Int =
        ContextCompat.getColor(placeHolder.context, R.color.colorAccent)

    private val view: View by lazy {
        with(
            LayoutInflater.from(placeHolder.context).inflate(
                R.layout.loading_state_presentation,
                placeHolder,
                false
            )
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                colorProgressBar(this)
            }
            return@lazy this
        }
    }

    override fun showState(state: MainLoadingState) {
        placeHolder.changeViewTo(view)
        placeHolder.show()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun colorProgressBar(view: View) {
        val progressBar: ProgressBar = view.findViewById(R.id.placeholder_loading_pb)
        progressBar.indeterminateTintList = ColorStateList.valueOf(progressBarColor)
    }
}