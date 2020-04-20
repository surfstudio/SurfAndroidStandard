package ru.surfstudio.standard.f_main.view

import android.content.Context
import android.graphics.drawable.Animatable
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.view_bottom_bar_item.view.*
import ru.surfstudio.android.template.f_main.R
import ru.surfstudio.android.utilktx.ktx.text.EMPTY_STRING

/**
 * Вью кнопки в bottom bar
 */
class BottomBarItemView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        inflate(context, R.layout.view_bottom_bar_item, this)
        initAttrs(attrs)
    }

    /**
     * @param isChecked true если таб выбран, иначе false
     */
    fun setChecked(isChecked: Boolean) {
        val colorRes = if (isChecked) R.color.colorAccent else android.R.color.black
        val colorFilter = ContextCompat.getColor(context, colorRes)
        bottom_bar_item_iv.setColorFilter(colorFilter)
        bottom_bar_item_label.setTextColor(colorFilter)

        if (isChecked) animateIcon()
    }

    /**
     * @param value значение счетчика
     */
    fun setBadge(value: Int) {
        val isNotZero = value != 0
        badge_count_tv.isVisible = isNotZero
        
        if (isNotZero) {
            badge_count_tv.text = value.toString()
        }
    }

    private fun animateIcon() {
        val drawable = bottom_bar_item_iv.drawable
        if (drawable is Animatable) {
            drawable.start()
        }
    }

    private fun initAttrs(attributeSet: AttributeSet?) {
        val ta = context.obtainStyledAttributes(attributeSet, R.styleable.BottomBarItemView, 0, 0)
        val labelText = ta.getString(R.styleable.BottomBarItemView_item_labelText) ?: EMPTY_STRING
        val iconRes = ta.getResourceId(R.styleable.BottomBarItemView_item_icon, -1)

        bottom_bar_item_label.text = labelText
        bottom_bar_item_iv.setImageDrawable(ContextCompat.getDrawable(context, iconRes))

        ta.recycle()
    }
}