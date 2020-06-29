package ru.surfstudio.standard.ui.view.keyboard

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.FontRes
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

    var textSize: Float = DEFAULT_TEXT_SIZE
        set(value) {
            field = value
            textPaint.textSize = value
        }

    @ColorInt
    var textColor: Int = DEFAULT_TEXT_COLOR
        set(value) {
            field = value
            textPaint.color = value
        }

    @FontRes
    var font: Int = UNDEFINE_ATTR
        set(value) {
            field = value
            if (value != UNDEFINE_ATTR) {
                textPaint.typeface = ResourcesCompat.getFont(context, value)
            }
        }

    private val contentPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint: Paint = TextPaint(Paint.ANTI_ALIAS_FLAG)

    init {
        initAttrs(attrs, defStyleAttrs, defStyleRes)
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

    private fun initAttrs(attrs: AttributeSet?, defStyleAttrs: Int, defStyleRes: Int) {
        context.obtainStyledAttributes(attrs, R.styleable.KeyView, defStyleAttrs, defStyleRes).apply {
            textColor = getColor(R.styleable.KeyView_android_textColor, DEFAULT_TEXT_COLOR)
            textSize = getDimension(R.styleable.KeyView_android_textSize, DEFAULT_TEXT_SIZE)
            font = getResourceId(R.styleable.KeyView_font, UNDEFINE_ATTR)
        }.recycle()
    }

    companion object {

        const val DEFAULT_TEXT_SIZE = 14F
        const val DEFAULT_TEXT_COLOR = Color.WHITE
        const val UNDEFINE_ATTR = -1
    }
}