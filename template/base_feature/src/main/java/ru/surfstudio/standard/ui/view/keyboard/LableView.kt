package ru.surfstudio.standard.ui.view.keyboard

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import ru.surfstudio.android.template.base_feature.R
import ru.surfstudio.android.utilktx.ktx.convert.toBitmap

/**
 * View для рисования текста и иконки на [Canvas]
 */
open class LableView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttrs: Int = 0,
        defStyleRes: Int = 0
) : View(context, attrs, defStyleAttrs/*, defStyleRes  todo*/) {

    protected val contentPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    protected val textPaint: Paint = TextPaint(Paint.ANTI_ALIAS_FLAG)

    //todo необходимо парсить textAppearance.
    init {
        with(context.obtainStyledAttributes(attrs, R.styleable.LableView, defStyleAttrs, defStyleRes)) {

            val textColor = getColor(R.styleable.LableView_textColor, Color.WHITE)
            setTextColor(textColor)

            val textSize = getDimension(R.styleable.LableView_textSize, 14f)
            setTextSize(textSize)

            val fontRes = getResourceId(R.styleable.LableView_font, -1)//todo
            if (fontRes != -1) {
                setFont(ResourcesCompat.getFont(context, fontRes))
            }
            recycle()
        }
    }

    fun setTextSize(size: Float) {
        textPaint.textSize = size
    }

    fun setFont(font: Typeface?) {
        textPaint.typeface = font
    }

    fun setTextColor(@ColorInt color: Int) {
        textPaint.color = color
    }

    protected fun draw(canvas: Canvas, text: String) {
        with(text) {
            val x = width.div(2) - textPaint.measureText(this).div(2)
            val y = height.div(2) - (textPaint.descent() + textPaint.ascent()).div(2)
            canvas.drawText(this, x, y, textPaint)
        }
    }

    protected fun draw(canvas: Canvas, @DrawableRes icon: Int) {
        ContextCompat.getDrawable(context, icon)?.let {
            draw(canvas, it)
        }
    }

    protected fun draw(canvas: Canvas, icon: Drawable) {
        draw(canvas, icon.toBitmap())
    }

    protected fun draw(canvas: Canvas, icon: Bitmap) {
        canvas.drawBitmap(icon, (width.div(2) - icon.width.div(2)).toFloat(), (height.div(2) - icon.height.div(2)).toFloat(), contentPaint)
    }
}