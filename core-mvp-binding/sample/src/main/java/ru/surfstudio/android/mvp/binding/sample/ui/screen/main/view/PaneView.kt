package ru.surfstudio.android.mvp.binding.sample.ui.screen.main.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatTextView

class PaneView @JvmOverloads constructor(context: Context,
                                         attrs: AttributeSet? = null,
                                         defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    var listener: ((String) -> Unit)? = null

    init {
        gravity = Gravity.CENTER

        setOnClickListener {
            val newValue = text.toString().toInt() + 1
            text = newValue.toString()
            listener?.invoke(text.toString())
        }
    }

    fun setPressed() {
        setBackgroundColor(Color.CYAN)
    }

    fun setUnpressed() {
        setBackgroundColor(Color.GRAY)
    }
}