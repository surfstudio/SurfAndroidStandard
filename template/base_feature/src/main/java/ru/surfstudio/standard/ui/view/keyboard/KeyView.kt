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
import ru.surfstudio.android.template.base_feature.R
import ru.surfstudio.android.utilktx.ktx.convert.toBitmap
import ru.surfstudio.android.utilktx.util.ViewUtil

/**
 * View для рисования иконки на [Canvas]
 * todo удалить, если не требуется на проекте
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

    var titleTextSize: Float = DEFAULT_TITLE_SIZE
        set(value) {
            field = value
            titlePaint.textSize = value
            invalidate()
        }

    var subtitleTextSize: Float = DEFAULT_SUBTITLE_SIZE
        set(value) {
            field = value
            subtitlePaint.textSize = value
            invalidate()
        }

    @ColorInt
    var titleColor: Int = DEFAULT_TITLE_COLOR
        set(value) {
            field = value
            titlePaint.color = value
            invalidate()
        }

    @ColorInt
    var subtitleColor: Int = DEFAULT_SUBTITLE_COLOR
        set(value) {
            field = value
            subtitlePaint.color = value
            invalidate()
        }

    @FontRes
    var titleFont: Int = UNDEFINE_ATTR
        set(value) {
            field = value
            if (value != UNDEFINE_ATTR) {
                titlePaint.typeface = ResourcesCompat.getFont(context, value)
                invalidate()
            }
        }

    @FontRes
    var subtitleFont: Int = UNDEFINE_ATTR
        set(value) {
            field = value
            if (value != UNDEFINE_ATTR) {
                subtitlePaint.typeface = ResourcesCompat.getFont(context, value)
                invalidate()
            }
        }

    var subtitleMargin = DEFAULT_SUBTITLE_MARGIN
        set(value) {
            field = value
            invalidate()
        }

    var isSubtitleVisible = true
        set(value) {
            field = value
            invalidate()
        }

    private val contentPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val titlePaint: Paint = TextPaint(Paint.ANTI_ALIAS_FLAG)
    private val subtitlePaint: Paint = TextPaint(Paint.ANTI_ALIAS_FLAG)

    init {
        initAttrs(attrs, defStyleAttrs, defStyleRes)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        when (key) {
            is BaseTextKey -> {
                if (isSubtitleVisible) {
                    draw(canvas, (key as BaseTextKey).title, (key as BaseTextKey).subtitle)
                } else {
                    draw(canvas, (key as BaseTextKey).title)
                }
            }
            is BaseIconKey -> draw(canvas, (key as BaseIconKey).icon)
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

    private fun initAttrs(attrs: AttributeSet?, defStyleAttrs: Int, defStyleRes: Int) {
        context.obtainStyledAttributes(attrs, R.styleable.KeyView, defStyleAttrs, defStyleRes).apply {
            isSubtitleVisible = getBoolean(R.styleable.KeyView_isSubtitleVisible, true)

            titleFont = getResourceId(R.styleable.KeyView_titleFont, UNDEFINE_ATTR)
            titleColor = getColor(R.styleable.KeyView_titleTextColor, DEFAULT_TITLE_COLOR)
            titleTextSize = getDimension(
                    R.styleable.KeyView_titleTextSize,
                    ViewUtil.convertDpToPx(context, DEFAULT_TITLE_SIZE).toFloat()
            )

            subtitleFont = getResourceId(R.styleable.KeyView_subtitleFont, UNDEFINE_ATTR)
            subtitleColor = getColor(R.styleable.KeyView_subtitleTextColor, DEFAULT_TITLE_COLOR)
            subtitleTextSize = getDimension(
                    R.styleable.KeyView_subtitleTextSize,
                    ViewUtil.convertDpToPx(context, DEFAULT_TITLE_SIZE).toFloat()
            )

            subtitleMargin = getDimension(
                    R.styleable.KeyView_subtitle_margin,
                    ViewUtil.convertDpToPx(context, DEFAULT_SUBTITLE_MARGIN).toFloat()
            )
        }.recycle()
    }

    companion object {

        const val DEFAULT_TITLE_COLOR = Color.BLACK
        const val DEFAULT_SUBTITLE_COLOR = Color.GRAY
        const val DEFAULT_TITLE_SIZE = 38f //sp
        const val DEFAULT_SUBTITLE_SIZE = 12f //sp
        const val DEFAULT_SUBTITLE_MARGIN = 10f //dp
        const val UNDEFINE_ATTR = -1
    }
}