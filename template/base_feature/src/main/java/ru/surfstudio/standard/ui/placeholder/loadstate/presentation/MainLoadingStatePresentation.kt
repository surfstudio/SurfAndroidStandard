package ru.surfstudio.standard.ui.placeholder.loadstate.presentation

import android.view.LayoutInflater
import ru.surfstudio.android.core.mvp.loadstate.SimpleLoadStatePresentation
import ru.surfstudio.android.custom.view.placeholder.PlaceHolderViewContainer
import ru.surfstudio.android.custom.view.placeholder.setClickableAndFocusable
import ru.surfstudio.standard.ui.placeholder.loadstate.state.MainLoadingState
import ru.surfstudio.android.template.base_feature.databinding.LayoutLoadStateBinding

/**
 * Представление состояния MainLoading в виде ProgressBar
 */
class MainLoadingStatePresentation(
        private val placeHolder: PlaceHolderViewContainer
) : SimpleLoadStatePresentation<MainLoadingState>() {

    private val binding = LayoutLoadStateBinding.inflate(LayoutInflater.from(placeHolder.context))

    override fun showState(state: MainLoadingState) {
        with(placeHolder) {
            changeViewTo(binding.root)
            setClickableAndFocusable(true)
            show()
        }
    }
}