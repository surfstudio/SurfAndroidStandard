package ru.surfstudio.android.recycler.extension.sample.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import ru.surfstudio.android.recycler.extension.sample.R

internal class ShareSlidingButtonView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : BaseSlidingButtonView(context, attrs, defStyleAttr) {

    init {
        setText("Share")
        setIconRes(R.drawable.ic_share)
        setContentColor(Color.parseColor("#ccccff"))
    }

}