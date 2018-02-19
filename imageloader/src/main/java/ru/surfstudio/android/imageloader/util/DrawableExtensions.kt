package ru.surfstudio.android.imageloader.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable

/**
 * Вспомогательные методы для работы с [Drawable]
 */

/**
 * Конвертация [Drawable] в [Bitmap]
 */
fun Drawable.toBitmap(): Bitmap {
    val bitmap = Bitmap.createBitmap(this.intrinsicWidth, this.intrinsicHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    this.setBounds(0, 0, canvas.width, canvas.height)
    this.draw(canvas)
    return bitmap
}