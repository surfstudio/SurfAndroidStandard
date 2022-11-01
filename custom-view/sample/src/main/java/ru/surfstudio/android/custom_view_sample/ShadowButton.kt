package ru.surfstudio.android.custom_view_sample

import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.util.AttributeSet
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.core.animation.doOnEnd
import ru.surfstudio.android.custom.view.shadow.ShadowLayout
import ru.surfstudio.android.utilktx.ktx.text.EMPTY_STRING
import ru.surfstudio.android.utilktx.ktx.ui.view.setDrawableColor
import ru.surfstudio.android.utilktx.util.SdkUtils

private const val DEFAULT_ANIMATION_DURATION = 250L

/** Кнопка с тенью. */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class ShadowButton(
        context: Context,
        attrs: AttributeSet? = null
) : ShadowLayout(context, attrs, R.attr.shadowButtonStyle) {

    /** Текст этой [ShadowButton] */
    var text: CharSequence
        get() = buttonView.text
        set(value) {
            buttonView.text = value
        }

    private var currentAnimation: ValueAnimator? = null
    private var bufferShadowAlpha: Int = DEFAULT_ALPHA_PERCENT

    private val buttonView: Button

    init {
        context.obtainStyledAttributes(attrs, R.styleable.ShadowButton).apply {
            val buttonStyleRes = getResourceId(R.styleable.ShadowButton_buttonStyle, R.style.DefaultButton_Primary)
            val buttonText = getString(R.styleable.ShadowButton_buttonText) ?: EMPTY_STRING
            val isButtonEnabled = getBoolean(R.styleable.ShadowButton_android_enabled, true)
            recycle()

            buttonView = Button(context, null, 0, buttonStyleRes).also {
                addView(it, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
            }

            // Хак, используемый для устройств версии ниже 24,
            // приходится вручную вынимать цвет drawable из стиля  и применять его на кнопку.
            if (!SdkUtils.isAtLeastNougat()) {
                context.obtainStyledAttributes(buttonStyleRes, intArrayOf(android.R.attr.drawableTint)).apply {
                    buttonView.setDrawableTintCompat(getColor(0, Color.BLACK))
                    recycle()
                }
            }

            bufferShadowAlpha = shadowAlphaPercent
            isEnabled = isButtonEnabled
            text = buttonText
        }
        setAddStatesFromChildren(true)
    }

    override fun setOnClickListener(listener: OnClickListener?) {
        buttonView.setOnClickListener(listener)
    }

    override fun setEnabled(isEnabled: Boolean) {
        super.setEnabled(isEnabled)
        buttonView.isEnabled = isEnabled
        shouldDrawShadow = isEnabled
    }

    override fun drawableStateChanged() {
        super.drawableStateChanged()
        changeShadowOpacity()
    }

    private fun changeShadowOpacity() {
        val toValue = if (android.R.attr.state_pressed in drawableState) {
            ALPHA_TRANSPARENT_INT
        } else {
            this.bufferShadowAlpha
        }
        currentAnimation?.cancel()
        currentAnimation = changeValue(
                this.shadowAlphaPercent,
                toValue,
                DEFAULT_ANIMATION_DURATION,
                onUpdate = { this.shadowAlphaPercent = it }
        )
    }

    /** Устанавливает цвет у `compoundDrawables` специфичным для используемой версии OS алгоритмом. */
    private fun Button.setDrawableTintCompat(color: Int) {
        when {
            SdkUtils.isAtLeastNougat() -> compoundDrawableTintList = ColorStateList.valueOf(color)
            else -> setDrawableColor(color)
        }
    }

    private inline fun changeValue(
            from: Int,
            to: Int,
            duration: Long,
            startDelay: Long = 0L,
            crossinline onUpdate: (value: Int) -> Unit,
            crossinline onComplete: () -> Unit = {}
    ): ValueAnimator {
        val vH: PropertyValuesHolder = PropertyValuesHolder.ofInt("prop", from, to)

        return ValueAnimator.ofPropertyValuesHolder(vH).apply {
            this.duration = duration
            this.startDelay = startDelay
            addUpdateListener {
                onUpdate(this.getAnimatedValue("prop") as Int)
            }
            doOnEnd { onComplete() }
            start()
        }
    }

    private companion object {
        const val ALPHA_TRANSPARENT_INT = 0
    }
}
