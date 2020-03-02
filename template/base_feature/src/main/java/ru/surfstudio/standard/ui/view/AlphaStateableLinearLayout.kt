package ru.surfstudio.standard.ui.view

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import ru.surfstudio.android.template.base_feature.R
import ru.surfstudio.standard.ui.utils.changeValue

private const val STATE_PRESSED_ALPHA = 0.7f
private const val STATE_NORMAL_ALPHA = 1f
private const val ALPHA_ANIMATION_DURATION = 250L

/**
 * LinearLayout со свойством подмены прозрачности в зависимости от состояния (state_pressed)
 * По умолчанию при pressed_state вью получает 70% прозрачности.
 *
 * @property statePressedAlpha прозрачность при нажатом состоянии
 * @property stateNormalAlpha прозрачность при нормальном состоянии
 *
 * TODO нужно удалить (а также все связанные ресурсы) если в проекте нигде не используется
 */
class AlphaStateableLinearLayout @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

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
        currentAnimation = changeValue(alpha, endAlpha, ALPHA_ANIMATION_DURATION) {
            alpha = it
        }
    }

    private fun initAttrs(attrs: AttributeSet?) {
        attrs?.let {
            val a = context.theme.obtainStyledAttributes(attrs, R.styleable.AlphaStateableLinearLayout, 0, 0)
            statePressedAlpha = a.getFloat(R.styleable.AlphaStateableLinearLayout_statePressedAlpha, STATE_PRESSED_ALPHA)
            stateNormalAlpha = a.getFloat(R.styleable.AlphaStateableLinearLayout_stateNormalAlpha, STATE_NORMAL_ALPHA)
            a.recycle()
        }
    }
}