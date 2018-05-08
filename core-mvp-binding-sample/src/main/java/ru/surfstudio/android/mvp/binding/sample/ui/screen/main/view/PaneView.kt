package ru.surfstudio.android.mvp.binding.sample.ui.screen.main.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.TextView

class PaneView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : TextView(context, attrs, defStyleAttr) {

    var listener: ((String) -> Unit)? = null

    init {
        setOnClickListener { listener?.invoke(text.toString()) }
    }

    fun setPressed() {
        setBackgroundColor(Color.CYAN)
    }

    fun setUnpressed() {
        setBackgroundColor(Color.GRAY)
    }
}