package ru.surfstudio.android.recycler.extension.sample.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import kotlinx.android.synthetic.main.layout_base_sliding_button_view.view.*
import ru.surfstudio.android.recycler.extension.sample.R

/**
 * Base view for use in [ru.surfstudio.android.recycler.extension.slide.BindableSlidingViewHolder]
 * with customization.
 * */
internal abstract class BaseSlidingButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT)
        View.inflate(context, R.layout.layout_base_sliding_button_view, this)
        setBackgroundColor(Color.parseColor("#333333"))
        setContentColor(Color.parseColor("#FFFFFF"))
    }

    override fun setOnClickListener(listener: OnClickListener?) {
        base_sliding_button_clickable_v.setOnClickListener(listener)
    }

    /** Set icon for thi sliding button. */
    fun setIconRes(@DrawableRes resId: Int) {
        base_sliding_button_icon_iv.setImageResource(resId)
    }

    /** Set text for this sliding button. */
    fun setText(text: String) {
        base_sliding_button_title_tv.text = text
    }

    /** Set text and icon colors for this sliding button. */
    fun setContentColor(@ColorInt colorInt: Int) {
        base_sliding_button_icon_iv.setColorFilter(colorInt)
        base_sliding_button_title_tv.setTextColor(colorInt)
    }

}