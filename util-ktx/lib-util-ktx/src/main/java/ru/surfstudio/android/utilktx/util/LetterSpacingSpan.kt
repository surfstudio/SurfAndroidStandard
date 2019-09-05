/*
  Copyright (c) 2018-present, SurfStudio LLC.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
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