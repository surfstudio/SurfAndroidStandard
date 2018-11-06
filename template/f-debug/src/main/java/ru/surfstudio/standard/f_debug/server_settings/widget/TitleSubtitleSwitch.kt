package ru.surfstudio.standard.f_debug.server_settings.widget

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.CompoundButton
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.layout_title_subtitle_switch.view.*
import ru.surfstudio.android.template.f_debug.R

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class TitleSubtitleSwitch(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    private var isInflated: Boolean

    var titleText: String = ""
        set(value) {
            field = value
            if (isInflated) {
                triggerSetters()
            }
        }
    var isChecked: Boolean = false
        set(value) {
            field = value
            if (isInflated) {
                triggerSetters()
            }
        }

    init {
        isInflated = false
        obtainAttributes(context, attrs)
    }

    private fun obtainAttributes(context: Context, attributeSet: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.TitleSubtitleSwitch)
        titleText = typedArray.getString(R.styleable.TitleSubtitleSwitch_switch_title) ?: ""
        isChecked = typedArray.getBoolean(R.styleable.TitleSubtitleSwitch_switch_checked, false)
        typedArray.recycle()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        isInflated = true
        initViews()
        triggerSetters()
    }

    private fun initViews() {
        addView(LayoutInflater.from(context).inflate(R.layout.layout_title_subtitle_switch, this, false))
        initOnClickListeners()
    }

    private fun initOnClickListeners() {
        setOnClickListener {
            isChecked = !isChecked
            triggerSetters()
        }
        value_switch.setOnClickListener {
            isChecked = !isChecked
            triggerSetters()
        }
    }

    private fun triggerSetters() {
        title_tv.text = titleText
        value_switch.isChecked = isChecked
        setSubtitleText()
    }

    private fun setSubtitleText() {
        subtitle_tv.text = if (isChecked) {
            resources.getString(R.string.switch_subtitle_on_text)
        } else {
            resources.getString(R.string.switch_subtitle_off_text)
        }
    }

    fun setOnCheckedChangeListener(listener: ((CompoundButton, Boolean) -> Unit)) {
        value_switch.setOnCheckedChangeListener(listener)
    }

    fun setOnCheckedChangeListener(listener: CompoundButton.OnCheckedChangeListener) {
        value_switch.setOnCheckedChangeListener(listener)
    }

    override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()
        val savedState = SavedState(superState)
        savedState.titleText = titleText
        savedState.isChecked = isChecked
        return savedState
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        val savedState: SavedState = state as SavedState
        super.onRestoreInstanceState(savedState.superState)
        titleText = savedState.titleText
        isChecked = savedState.isChecked
    }


    private class SavedState : BaseSavedState {

        var titleText: String = ""
        var isChecked: Boolean = false

        constructor(superState: Parcelable) : super(superState)

        constructor(source: Parcel) : super(source) {
            titleText = source.readString()
            isChecked = source.readInt() != 0
        }

        @Suppress("UNUSED")
        private val creator: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {
            override fun createFromParcel(source: Parcel): SavedState {
                return SavedState(source)
            }

            override fun newArray(size: Int): Array<SavedState> {
                return emptyArray()
            }
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeString(titleText)
            out.writeInt(if (isChecked) 1 else 0)
        }
    }
}