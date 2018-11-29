package ru.surfstudio.standard.base_ui.loadstate.presentation

import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import ru.surfstudio.standard.base_ui.loadstate.PlaceHolderViewContainer
import ru.surfstudio.android.core.mvp.loadstate.SimpleLoadStatePresentation
import ru.surfstudio.standard.base_ui.loadstate.state.ErrorLoadState
import ru.surfstudio.android.core.mvp.loadstate.LoadStateInterface
import ru.surfstudio.android.template.base_ui.R

/**
 * Представление состояния ErrorLoadState, с картинкой, тайтлом, сабтайтлом и кнопкой
 */
class ErrorLoadStatePresentation(
        private val placeHolder: PlaceHolderViewContainer
) : SimpleLoadStatePresentation<ErrorLoadState>() {

    @StringRes
    var messageText: Int = R.string.load_state_error

    @ColorInt
    private val background = ContextCompat.getColor(placeHolder.context, R.color.colorWindowBackgroundDark)

    private lateinit var messageView: TextView

    private val view: View by lazy {
        LayoutInflater.from(placeHolder.context)
                .inflate(R.layout.load_state_error, placeHolder, false)
                .apply {
                    messageView = findViewById(R.id.error_load_state_tv)
                }
    }

    override fun showState(state: ErrorLoadState) {
        initViews(view)

        placeHolder.changeViewTo(view)
        placeHolder.show()
        placeHolder.setBackgroundColor(background)

        //Пустой листенер проставляется для перехвата кликов по элементам, которые перекрывает placeholder
        placeHolder.setOnClickListener { }
    }

    override fun hideState(state: ErrorLoadState, nextState: LoadStateInterface) {
        placeHolder.setBackgroundColor(ContextCompat.getColor(placeHolder.context, android.R.color.transparent))
    }

    private fun initViews(view: View) {
        messageView.text = view.context.getString(messageText)
    }
}