package ru.surfstudio.standard.f_main.view

import android.content.Context
import android.graphics.drawable.Animatable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import ru.surfstudio.android.template.f_main.R
import ru.surfstudio.android.template.f_main.databinding.ViewBottomBarItemBinding
import ru.surfstudio.android.utilktx.ktx.text.EMPTY_STRING

/**
 * Вью кнопки в bottom bar
 */
class BottomBarItemView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding: ViewBottomBarItemBinding = ViewBottomBarItemBinding.inflate(LayoutInflater.from(context), this)

    init {
        orientation = VERTICAL
        inflate(context, R.layout.view_bottom_bar_item, this)
        initAttrs(attrs)
    }

    /**
     * @param isChecked true если таб выбран, иначе false
     */
    fun setChecked(isChecked: Boolean) {
        val colorRes = if (isChecked) R.color.colorAccent else android.R.color.black
        val colorFilter = ContextCompat.getColor(context, colorRes)
        binding.bottomBarItemIv.setColorFilter(colorFilter)
        binding.bottomBarItemTv.setTextColor(colorFilter)

        if (isChecked) animateIcon()
    }

    /**
     * @param isVisible true, если бейдж нужно показать, false если скрыть
     */
    fun setBadgeVisibility(isVisible: Boolean) {
        binding.bottomBarItemBadge.isVisible = isVisible
    }

    /**
     * @param value значение счетчика
     */
    fun setCounter(value: Int) {
        with(binding){
            val isNotZero = value != 0
            bottomBarItemCountTv.isVisible = isNotZero

            if (isNotZero) {
                bottomBarItemCountTv.text = value.toString()
            }
        }
    }

    private fun animateIcon() {
        val drawable = binding.bottomBarItemIv.drawable
        if (drawable is Animatable) {
            drawable.start()
        }
    }

    private fun initAttrs(attributeSet: AttributeSet?) {
        val ta = context.obtainStyledAttributes(attributeSet, R.styleable.BottomBarItemView, 0, 0)
        val labelText = ta.getString(R.styleable.BottomBarItemView_item_labelText) ?: EMPTY_STRING
        val iconRes = ta.getResourceId(R.styleable.BottomBarItemView_item_icon, -1)

        with(binding){
            bottomBarItemTv.text = labelText
            bottomBarItemIv.setImageDrawable(ContextCompat.getDrawable(context, iconRes))
        }


        ta.recycle()
    }
}