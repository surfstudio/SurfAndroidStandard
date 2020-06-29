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
import ru.surfstudio.standard.ui.view.keyboard.keys.BaseIconKey
import ru.surfstudio.standard.ui.view.keyboard.keys.BaseTextKey
import ru.surfstudio.standard.ui.view.keyboard.keys.Key

/**
 * View для рисования иконки на [Canvas]
 */
class KeyView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttrs: Int = 0,
        defStyleRes: Int = 0
) : View(context, attrs, defStyleAttrs, defStyleRes) {

    var key: Key? = null
        set(value) {
            field = value
            invalidate()
        }

    private val contentPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint: Paint = TextPaint(Paint.ANTI_ALIAS_FLAG)

    //todo необходимо парсить textAppearance.
    init {
        with(context.obtainStyledAttributes(attrs, R.styleable.KeyView, defStyleAttrs, defStyleRes)) {

            val textColor = getColor(R.styleable.KeyView_textColor, Color.WHITE)
            setTextColor(textColor)

            val textSize = getDimension(R.styleable.KeyView_textSize, 14f)
            setTextSize(textSize)

            val fontRes = getResourceId(R.styleable.KeyView_font, -1)//todo
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

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        when (key) {
            is BaseTextKey -> draw(canvas, (key as BaseTextKey).title)
            is BaseIconKey -> draw(canvas, (key as BaseIconKey).icon)
            //ignore EmptyKey
        }
    }

    private fun draw(canvas: Canvas, text: String) {
        with(text) {
            val x = width.div(2) - textPaint.measureText(this).div(2)
            val y = height.div(2) - (textPaint.descent() + textPaint.ascent()).div(2)
            canvas.drawText(this, x, y, textPaint)
        }
    }

    private fun draw(canvas: Canvas, @DrawableRes icon: Int) {
        ContextCompat.getDrawable(context, icon)?.let {
            draw(canvas, it)
        }
    }

    private fun draw(canvas: Canvas, icon: Drawable) = draw(canvas, icon.toBitmap())

    private fun draw(canvas: Canvas, icon: Bitmap) =
            canvas.drawBitmap(
                    icon,
                    (width.div(2) - icon.width.div(2)).toFloat(),
                    (height.div(2) - icon.height.div(2)).toFloat(),
                    contentPaint
            )
}