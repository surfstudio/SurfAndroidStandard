package ru.surfstudio.standard.f_debug.common_widgets

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.CompoundButton
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.layout_title_subtitle_switch.view.*
import ru.surfstudio.android.template.f_debug.R

/**
 * Виджет, позволяющий задать как title, так и subtitle у Switch
 */
class TitleSubtitleSwitch(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {

    private val onCheckedChangeListener =
            CompoundButton.OnCheckedChangeListener { _, isChecked ->
                this.isChecked = isChecked
            }

    private var isChecked: Boolean = false

    var title: String = ""
        set(value) {
            title_tv.text = value
        }

    var subtitle: String = ""
        set(value) {
            subtitle_tv.text = value
        }


    init {
        View.inflate(context, R.layout.layout_title_subtitle_switch, this)

        initListeners()

        obtainAttributes(context, attrs)
    }

    fun isChecked() = isChecked

    fun setChecked(value: Boolean) {
        value_switch.isChecked = value
    }

    private fun initListeners() {
        value_switch.setOnCheckedChangeListener(onCheckedChangeListener)
        this.setOnClickListener { setChecked(!value_switch.isChecked) }
    }

    private fun obtainAttributes(context: Context, attributeSet: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.TitleSubtitleSwitch)
        value_switch.isChecked = typedArray.getBoolean(R.styleable.TitleSubtitleSwitch_switch_checked, isChecked)
        title = typedArray.getString(R.styleable.TitleSubtitleSwitch_switch_title) ?: ""
        subtitle = typedArray.getString(R.styleable.TitleSubtitleSwitch_switch_subtitle) ?: ""
        typedArray.recycle()
    }

    fun setOnCheckedChangeListener(listener: (CompoundButton, Boolean) -> Unit) {
        value_switch.setOnClickListener {
            onCheckedChangeListener.onCheckedChanged(value_switch, value_switch.isChecked)
            listener(value_switch, isChecked)
        }
    }
}