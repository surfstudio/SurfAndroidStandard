package ru.surfstudio.standard.ui.view.keyboard

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.FontRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import ru.surfstudio.android.utilktx.ktx.convert.toBitmap

/**
 * Вью кнопки клавиатуры
 * todo удалить, если не требуется на проекте
 */
class KeyView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttrs: Int = 0,
        defStyleRes: Int = 0
) : View(context, attrs, defStyleAttrs, defStyleRes) {

    var key: Key? = null

    var titleTextSize: Float = DEFAULT_TITLE_SIZE_SP
        set(value) {
            field = value
            titlePaint.textSize = value
        }

    var subtitleTextSize: Float = DEFAULT_SUBTITLE_SIZE_SP
        set(value) {
            field = value
            subtitlePaint.textSize = value
        }

    @ColorInt
    var titleColor: Int = DEFAULT_TITLE_COLOR
        set(value) {
            field = value
            titlePaint.color = value
        }

    @ColorInt
    var subtitleColor: Int = DEFAULT_SUBTITLE_COLOR
        set(value) {
            field = value
            subtitlePaint.color = value
        }

    @FontRes
    var titleFont: Int = UNDEFINE_ATTR
        set(value) {
            field = value
            if (value != UNDEFINE_ATTR) {
                titlePaint.typeface = ResourcesCompat.getFont(context, value)
            }
        }

    @FontRes
    var subtitleFont: Int = UNDEFINE_ATTR
        set(value) {
            field = value
            if (value != UNDEFINE_ATTR) {
                subtitlePaint.typeface = ResourcesCompat.getFont(context, value)
            }
        }

    var subtitleMargin = DEFAULT_SUBTITLE_MARGIN_DP
    var isSubtitleVisible = true

    private val contentPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val titlePaint: Paint = TextPaint(Paint.ANTI_ALIAS_FLAG)
    private val subtitlePaint: Paint = TextPaint(Paint.ANTI_ALIAS_FLAG)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        when (key) {
            is TextKey -> {
                if (isSubtitleVisible) {
                    draw(canvas, (key as TextKey).title, (key as TextKey).subtitle)
                } else {
                    draw(canvas, (key as TextKey).title)
                }
            }
            is IconKey -> draw(canvas, (key as IconKey).icon)
            //ignore EmptyKey
        }
    }

    private fun draw(canvas: Canvas, text: String) {
        with(text) {
            val x = width.div(2) - titlePaint.measureText(this).div(2)
            val y = height.div(2) - (titlePaint.descent() + titlePaint.ascent()).div(2)
            canvas.drawText(this, x, y, titlePaint)
        }
    }

    private fun draw(canvas: Canvas, title: String, subtitle: String) {
        val subtitleHeight = Rect().apply {
            subtitlePaint.getTextBounds(subtitle, 0, subtitle.length, this)
        }.height()
        val viewsMargin = (subtitleMargin + subtitleHeight) / 2

        val titleX = width.div(2) - titlePaint.measureText(title).div(2)
        val titleY = height.div(2) - (titlePaint.descent() + titlePaint.ascent()).div(2)
        canvas.drawText(title, titleX, titleY - viewsMargin, titlePaint)


        val subtitleX = width.div(2) - subtitlePaint.measureText(subtitle).div(2)
        val subtitleY = titleY
        canvas.drawText(subtitle, subtitleX, subtitleY + viewsMargin, subtitlePaint)
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

    companion object {

        const val DEFAULT_TITLE_COLOR = Color.BLACK
        const val DEFAULT_SUBTITLE_COLOR = Color.GRAY
        const val DEFAULT_TITLE_SIZE_SP = 38f
        const val DEFAULT_SUBTITLE_SIZE_SP = 12f
        const val DEFAULT_SUBTITLE_MARGIN_DP = 10f
        const val UNDEFINE_ATTR = -1
    }
}