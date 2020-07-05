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
class DebugTitleSubtitleSwitch(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {

    private val onCheckedChangeListener =
            CompoundButton.OnCheckedChangeListener { _, isChecked ->
                this.isChecked = isChecked
            }

    private var isChecked: Boolean = false

    var title: String = ""
        set(value) {
            debug_title_tv.text = value
        }

    var subtitle: String = ""
        set(value) {
            debug_subtitle_tv.text = value
        }


    init {
        View.inflate(context, R.layout.layout_title_subtitle_switch, this)

        initListeners()

        obtainAttributes(context, attrs)
    }

    fun isChecked() = isChecked

    fun setChecked(value: Boolean) {
        debug_value_switch.isChecked = value
    }

    private fun initListeners() {
        debug_value_switch.setOnCheckedChangeListener(onCheckedChangeListener)
        this.setOnClickListener { setChecked(!debug_value_switch.isChecked) }
    }

    private fun obtainAttributes(context: Context, attributeSet: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.DebugTitleSubtitleSwitch)
        debug_value_switch.isChecked = typedArray.getBoolean(R.styleable.DebugTitleSubtitleSwitch_debug_switch_checked, isChecked)
        title = typedArray.getString(R.styleable.DebugTitleSubtitleSwitch_debug_switch_title) ?: ""
        subtitle = typedArray.getString(R.styleable.DebugTitleSubtitleSwitch_debug_switch_subtitle) ?: ""
        typedArray.recycle()
    }

    fun setOnCheckedChangeListener(listener: (CompoundButton, Boolean) -> Unit) {
        debug_value_switch.setOnClickListener {
            onCheckedChangeListener.onCheckedChanged(debug_value_switch, debug_value_switch.isChecked)
            listener(debug_value_switch, isChecked)
        }
    }
}