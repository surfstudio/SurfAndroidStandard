package ru.surfstudio.android.imageloader.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat

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

/**
 * Преобразование [Drawable] из /res/drawable в [Bitmap]
 *
 * @param resId ссылка на drawable ресурс
 */
fun getBitmapFromRes(context: Context, @DrawableRes resId: Int): Bitmap? {
    val drawable = ContextCompat.getDrawable(context, resId)
    return when (drawable) {
        is BitmapDrawable -> {
            drawable.bitmap
        }
        is Drawable -> {
            drawable.toBitmap()
        }
        else -> {
            throw IllegalArgumentException("Неподдерживаемый тип Drawable - ${drawable?.javaClass?.canonicalName}")
        }
    }
}