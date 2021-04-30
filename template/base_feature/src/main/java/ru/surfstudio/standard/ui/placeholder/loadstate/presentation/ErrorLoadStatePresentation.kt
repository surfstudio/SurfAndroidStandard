package ru.surfstudio.standard.ui.placeholder.loadstate.presentation

import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.annotation.StringRes
import ru.surfstudio.android.core.mvp.loadstate.SimpleLoadStatePresentation
import ru.surfstudio.android.custom.view.placeholder.PlaceHolderViewContainer
import ru.surfstudio.android.custom.view.placeholder.setClickableAndFocusable
import ru.surfstudio.android.template.base_feature.R
import ru.surfstudio.android.template.base_feature.databinding.LayoutStateErrorBinding
import ru.surfstudio.standard.ui.placeholder.loadstate.state.ErrorLoadState

/**
 * Представление состояния ErrorLoadState, с картинкой, тайтлом, сабтайтлом и кнопкой
 */
class ErrorLoadStatePresentation(
        private val placeHolder: PlaceHolderViewContainer
) : SimpleLoadStatePresentation<ErrorLoadState>() {

    private val binding = LayoutStateErrorBinding.inflate(LayoutInflater.from(placeHolder.context))

    @StringRes
    var messageTextRes: Int = R.string.state_error_message

    override fun showState(state: ErrorLoadState) {
        initViews()
        with(placeHolder) {
            changeViewTo(binding.root)
            binding.errorLoadStateBtn.setOnClickListener { state.action() }
            setClickableAndFocusable(true)
            show()
        }
    }

    private fun initViews() {
        binding.errorLoadStateTv.text = binding.root.context.getString(messageTextRes)
    }
}