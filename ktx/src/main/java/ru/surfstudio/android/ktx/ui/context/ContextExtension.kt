package ru.surfstudio.android.ktx.ui.context

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.VectorDrawable
import android.support.annotation.DrawableRes
import android.support.graphics.drawable.VectorDrawableCompat
import android.support.v4.content.ContextCompat
import android.util.DisplayMetrics

/**
 *  Extension-методы для Context
 */

fun Context.getDisplayMetrics(): DisplayMetrics = resources.displayMetrics

/**
 * Возвращает Bitmap из Drawable
 */
fun Context.getBitmapFromDrawable(@DrawableRes drawableId: Int): Bitmap {
    val drawable = ContextCompat.getDrawable(this, drawableId)

    return if (drawable is BitmapDrawable) {
        drawable.bitmap
    } else if (drawable is VectorDrawableCompat || drawable is VectorDrawable) {
        val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        bitmap
    } else {
        throw IllegalArgumentException("unsupported drawable type")
    }
}