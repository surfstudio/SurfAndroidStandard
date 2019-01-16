package ru.surfstudio.standard.base_ui.loadstate.presentation

import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.annotation.StringRes
import ru.surfstudio.android.custom.view.placeholder.PlaceHolderViewContainer
import ru.surfstudio.android.core.mvp.loadstate.SimpleLoadStatePresentation
import ru.surfstudio.standard.base_ui.loadstate.state.ErrorLoadState
import ru.surfstudio.android.template.base_ui.R
import ru.surfstudio.android.custom.view.placeholder.setClickableAndFocusable

/**
 * Представление состояния ErrorLoadState, с картинкой, тайтлом, сабтайтлом и кнопкой
 */
class ErrorLoadStatePresentation(
        private val placeHolder: PlaceHolderViewContainer
) : SimpleLoadStatePresentation<ErrorLoadState>() {

    @StringRes
    var messageTextRes: Int = R.string.state_error_message

    private lateinit var messageView: TextView
    private lateinit var reloadButton: Button

    private val view: View by lazy {
        LayoutInflater.from(placeHolder.context)
                .inflate(R.layout.layout_state_error, placeHolder, false)
                .apply {
                    messageView = error_load_state_tv
                    reloadButton = error_load_state_b
                }
    }

    override fun showState(state: ErrorLoadState) {
        initViews(view)

        with(placeHolder) {
            changeViewTo(view)
            reloadButton.setOnClickListener { state.action() }
            setClickableAndFocusable(true)
            show()
        }
    }

    private fun initViews(view: View) {
        messageView.text = view.context.getString(messageTextRes)
    }
}