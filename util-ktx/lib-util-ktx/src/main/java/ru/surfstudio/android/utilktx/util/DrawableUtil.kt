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

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Animatable
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import androidx.core.content.ContextCompat
import android.widget.ImageView


/**
 * Утилита для работы с Drawable
 */
object DrawableUtil {

    /**
     * Конвертация Bitmap в Drawable
     */
    fun drawableToBitmap(drawable: Drawable): Bitmap {
        val bitmap: Bitmap = if (drawable.intrinsicWidth <= 0 || drawable.intrinsicHeight <= 0) {
            Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888) // Single color bitmap will be created of 1x1 pixel
        } else {
            Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        }

        if (drawable is BitmapDrawable) {
            if (drawable.bitmap != null) {
                return drawable.bitmap
            }
        }

        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
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
                drawableToBitmap(drawable)
            }
            else -> {
                throw IllegalArgumentException("Неподдерживаемый тип Drawable - ${drawable?.javaClass?.canonicalName}")
            }
        }
    }


    //Todo перенести в animations
    /**
     * утилита для запуска анимации у animated-vector
     */
    fun makeAvdAnimation(context: Context, imageView: ImageView, @DrawableRes drawableId: Int) {
        val avd = AnimatedVectorDrawableCompat.create(context, drawableId)
        imageView.setImageDrawable(avd)

        val drawable = imageView.drawable

        if (drawable is Animatable) {
            (drawable as Animatable).start()
        } else {
            throw IllegalArgumentException("Drawable must be Animatable")
        }
    }

    /**
     * Старт векторной анимации
     */
    fun startAvdAnimation(imageView: ImageView) {
        if (imageView.drawable !is AnimatedVectorDrawableCompat) {
            return
        }
        val animation = imageView.drawable as AnimatedVectorDrawableCompat
        animation.start()
    }
}