package ru.surfstudio.android.utilktx.ktx.convert

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import ru.surfstudio.android.utilktx.util.DrawableUtil

/**
 * Методы-конвертеры
 */

/**
 * Конвертация Bitmap в Drawable
 */
fun Drawable.toBitmap(): Bitmap = DrawableUtil.drawableToBitmap(this)


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