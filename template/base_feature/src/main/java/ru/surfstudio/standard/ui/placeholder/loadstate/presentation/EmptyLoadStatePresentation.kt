package ru.surfstudio.standard.ui.placeholder.loadstate.presentation

import android.view.LayoutInflater
import android.widget.TextView
import androidx.annotation.StringRes
import ru.surfstudio.android.core.mvp.loadstate.SimpleLoadStatePresentation
import ru.surfstudio.android.custom.view.placeholder.PlaceHolderViewContainer
import ru.surfstudio.android.custom.view.placeholder.setClickableAndFocusable
import ru.surfstudio.standard.ui.placeholder.loadstate.state.EmptyLoadState
import ru.surfstudio.android.template.base_feature.R
import ru.surfstudio.android.template.base_feature.databinding.LayoutStateEmptyBinding

/**
 * Представление состояния EmptyLoadState, с картинкой, тайтлом, сабтайтлом и кнопкой
 */
class EmptyLoadStatePresentation(
        private val placeHolder: PlaceHolderViewContainer
) : SimpleLoadStatePresentation<EmptyLoadState>() {

    private val binding = LayoutStateEmptyBinding.inflate(LayoutInflater.from(placeHolder.context))

    @StringRes
    var messageTextRes: Int = R.string.state_empty_text

    override fun showState(state: EmptyLoadState) {
        initViews()
        with(placeHolder) {
            changeViewTo(binding.root)
            setClickableAndFocusable(true)
            show()
        }
    }

    private fun initViews() {
        binding.emptyLoadStateTv.text = binding.root.context.getString(messageTextRes)
    }
}