package ru.surfstudio.android.ktx.convert

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import ru.surfstudio.android.util.DrawableUtil

/**
 * Методы-конвертеры
 */

/**
 * Конвертация Bitmap в Drawable
 */
fun Drawable.toBitmap(): Bitmap = DrawableUtil.drawableToBitmap(this)