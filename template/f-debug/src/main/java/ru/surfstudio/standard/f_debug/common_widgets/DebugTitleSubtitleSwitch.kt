package ru.surfstudio.standard.f_debug.common_widgets

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.CompoundButton
import androidx.constraintlayout.widget.ConstraintLayout
import ru.surfstudio.android.template.f_debug.R
import ru.surfstudio.android.template.f_debug.databinding.LayoutTitleSubtitleSwitchBinding

/**
 * Виджет, позволяющий задать как title, так и subtitle у Switch
 */
class DebugTitleSubtitleSwitch(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {

    private val binding = LayoutTitleSubtitleSwitchBinding.inflate(LayoutInflater.from(context), this)

    private val onCheckedChangeListener =
            CompoundButton.OnCheckedChangeListener { _, isChecked ->
                this.isChecked = isChecked
            }

    private var isChecked: Boolean = false

    var title: String = ""
        set(value) {
            binding.debugTitleTv.text = value
        }

    var subtitle: String = ""
        set(value) {
            binding.debugSubtitleTv.text = value
        }


    init {
        View.inflate(context, R.layout.layout_title_subtitle_switch, this)

        initListeners()

        obtainAttributes(context, attrs)
    }

    fun isChecked() = isChecked

    fun setChecked(value: Boolean) {
        binding.debugValueSwitch.isChecked = value
    }

    private fun initListeners() {
        binding.debugValueSwitch.setOnCheckedChangeListener(onCheckedChangeListener)
        this.setOnClickListener { setChecked(!binding.debugValueSwitch.isChecked) }
    }

    private fun obtainAttributes(context: Context, attributeSet: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.DebugTitleSubtitleSwitch)
        binding.debugValueSwitch.isChecked = typedArray.getBoolean(R.styleable.DebugTitleSubtitleSwitch_debug_switch_checked, isChecked)
        title = typedArray.getString(R.styleable.DebugTitleSubtitleSwitch_debug_switch_title) ?: ""
        subtitle = typedArray.getString(R.styleable.DebugTitleSubtitleSwitch_debug_switch_subtitle)
                ?: ""
        typedArray.recycle()
    }

    fun setOnCheckedChangeListener(listener: (CompoundButton, Boolean) -> Unit) {
        with(binding){
            debugValueSwitch.setOnClickListener {
                onCheckedChangeListener.onCheckedChanged(debugValueSwitch, debugValueSwitch.isChecked)
                listener(debugValueSwitch, isChecked)
            }
        }

    }
}