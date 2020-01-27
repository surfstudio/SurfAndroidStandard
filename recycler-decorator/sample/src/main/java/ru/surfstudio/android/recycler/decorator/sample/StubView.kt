package ru.surfstudio.android.recycler.decorator.sample

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.RequiresApi

/**
 * Dummy view for show simple shapes. (circle, square, square with rounded angles)
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class StubView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyles: Int = 0
) : View(context, attrs, defStyles) {

    @ColorInt
    var stubColor: Int = Color.WHITE
        set(value) {
            field = value
            paint.color = value
            invalidate()
        }
    private var isCircle: Boolean = false

    private var cornerRadius: Float = 0F

    private val paint: Paint = Paint(ANTI_ALIAS_FLAG)

    init {
        with(context.obtainStyledAttributes(attrs, R.styleable.StubView)) {
            isCircle = getBoolean(R.styleable.StubView_isCircle, false)
            cornerRadius = getDimensionPixelOffset(R.styleable.StubView_cornerRadius, 0).toFloat()
            stubColor = getColor(R.styleable.StubView_stubColor, Color.WHITE)
            recycle()
        }
        paint.color = stubColor
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (isCircle) {
            canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), (width / 2).toFloat(), paint)
        } else {
            canvas.drawRoundRect(0F, 0F, width.toFloat(), height.toFloat(), cornerRadius, cornerRadius, paint)
        }
    }
}