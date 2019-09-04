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
package ru.surfstudio.android.utilktx.ktx.convert

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
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