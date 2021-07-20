package ru.surfstudio.standard.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.StyleRes
import androidx.core.widget.TextViewCompat
import com.google.android.material.textfield.TextInputEditText
import ru.surfstudio.android.template.base_feature.R
import ru.surfstudio.android.utilktx.ktx.text.EMPTY_STRING

private const val NOT_DEFINED_RES = 0

/**
 * EditText with possibility specify postfix and prefix texts.
 * It is useful when you creating money input with hardcoded currency symbol.
 *
 * Must be used with [TextInputLayout]
 *
 * TODO нужно удалить (а также все связанные ресурсы) если в проекте нигде не используется
 */
class PostfixEditText @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : TextInputEditText(context, attrs) {

    private var postfixPaint: Paint = Paint(paint)
    private var postFixVisibility: (PostfixEditText) -> Boolean = {
        this.isFocused || this.text?.isNotEmpty() == true
    }

    /**
     * The text which will be shown after the actual text.
     * Postfix text will be shown when input has focus or the actual text is not empty.
     * Font size and typeface of the text will be chosen from actual text style.
     */
    var postfixText: String = EMPTY_STRING
        set(value) {
            field = value
            postInvalidate()
        }

    /**
     * Appearance of postfix text. By default it is same as editText's textAppearance.
     * It overrides the color property of postfix text. So you must set this property first
     * if you want to change the color separately.
     */
    @StyleRes
    var postfixTextAppearance: Int = NOT_DEFINED_RES
        set(value) {
            field = value
            if (value == NOT_DEFINED_RES) {
                postfixPaint = Paint(paint)
            } else {
                // at the moment it is impossible to set textAppearance differently
                val text = TextView(context)
                TextViewCompat.setTextAppearance(text, postfixTextAppearance)
                postfixPaint = text.paint
            }
            invalidate()
        }

    /**
     * Padding between postfix text and actual text in pixels.
     */
    var postfixPadding: Int = 0
        set(value) {
            field = value
            invalidate()
        }

    /**
     * Color of the postfix text.
     * By default, it equals to actual text color.
     */
    @ColorInt
    var postfixTextColor: Int = 0
        set(value) {
            field = value
            postfixPaint.color = value
            invalidate()
        }

    init {
        initAttr(context, attrs)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (postFixVisibility(this)) {
            val postfixPosition = paint.measureText(text.toString()) + postfixPadding + paddingStart
            canvas.drawText(postfixText, postfixPosition, baseline.toFloat(), postfixPaint)
        }
    }

    override fun getCompoundPaddingEnd(): Int {
        return super.getCompoundPaddingEnd() + postfixPadding + paint.measureText(postfixText).toInt()
    }

    /**
     * Sets the lambda that will calculate the visibility of the postfix text.
     */
    fun setPostFixVisibilityLambda(function: (PostfixEditText) -> Boolean) {
        this.postFixVisibility = function
    }

    private fun initAttr(context: Context, attrs: AttributeSet?) {
        if (attrs != null) {
            context.obtainStyledAttributes(attrs, R.styleable.PostfixEditText).apply {

                postfixTextAppearance = getResourceId(R.styleable.PostfixEditText_postfixTextAppearance, NOT_DEFINED_RES)
                postfixText = getString(R.styleable.PostfixEditText_postfixText) ?: EMPTY_STRING
                postfixTextColor = getInt(R.styleable.PostfixEditText_postfixTextColor, currentTextColor)
                postfixPadding = getDimensionPixelSize(R.styleable.PostfixEditText_postfixPadding, 0)

                recycle()
            }
        }
    }
}
