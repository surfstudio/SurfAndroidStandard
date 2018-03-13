package ru.surfstudio.android.utilktx.util


import android.graphics.Canvas
import android.graphics.Paint
import android.text.style.ReplacementSpan


/**
 * Спан, устанавливающий межсимвольные отступы.
 */
class LetterSpacingSpan(private val spacePx: Int) : ReplacementSpan() {

    override fun getSize(paint: Paint, text: CharSequence,
                         start: Int, end: Int, fm: Paint.FontMetricsInt?): Int {
        return (paint.measureText(text, start, end) + spacePx * (end - start - 1)).toInt()
    }

    override fun draw(canvas: Canvas, text: CharSequence,
                      start: Int, end: Int, x: Float, top: Int, y: Int,
                      bottom: Int, paint: Paint) {
        var dx = x
        for (i in start until end) {
            canvas.drawText(text, i, i + 1, dx, y.toFloat(), paint)
            dx += paint.measureText(text, i, i + 1) + spacePx
        }
    }
}