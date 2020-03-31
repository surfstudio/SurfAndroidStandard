package ru.surfstudio.android.custom.view.alphastate

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import ru.surfstudio.android.animations.anim.changeValue
import ru.surfstudio.android.custom.view.R

/**
 * ConstraintLayout со свойством подмены прозрачности в зависимости от состояния (state_pressed)
 * По умолчанию при pressed_state вью получает 70% прозрачности.
 *
 * @property statePressedAlpha прозрачность при нажатом состоянии
 * @property stateNormalAlpha прозрачность при нормальном состоянии
 */
class AlphaStateableConstraintLayout @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    private var currentAnimation: ValueAnimator? = null

    var statePressedAlpha = STATE_PRESSED_ALPHA
        set(value) {
            field = value
            changeOpacity()
        }

    var stateNormalAlpha = STATE_NORMAL_ALPHA
        set(value) {
            field = value
            changeOpacity()
        }

    init {
        initAttrs(attrs)
    }

    override fun drawableStateChanged() {
        super.drawableStateChanged()
        changeOpacity()
    }

    private fun changeOpacity() {
        if (android.R.attr.state_pressed in drawableState) {
            animateAlpha(statePressedAlpha)
        } else {
            animateAlpha(stateNormalAlpha)
        }
    }

    private fun animateAlpha(endAlpha: Float) {
        currentAnimation?.cancel()
        currentAnimation = changeValue(alpha, endAlpha, ALPHA_ANIMATION_DURATION,
                {
                    alpha = it
                }
        )
    }

    private fun initAttrs(attrs: AttributeSet?) {
        attrs?.let {
            val a = context.theme.obtainStyledAttributes(attrs, R.styleable.AlphaStateableConstraintLayout, 0, 0)
            statePressedAlpha = a.getFloat(R.styleable.AlphaStateableConstraintLayout_statePressedAlpha, STATE_PRESSED_ALPHA)
            stateNormalAlpha = a.getFloat(R.styleable.AlphaStateableConstraintLayout_stateNormalAlpha, STATE_NORMAL_ALPHA)
            a.recycle()
        }
    }
}