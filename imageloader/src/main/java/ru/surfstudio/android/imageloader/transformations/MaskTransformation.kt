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
package ru.surfstudio.android.imageloader.transformations

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import ru.surfstudio.android.utilktx.util.SdkUtils

/**
 * Маскирование изображения произвольной фигурой.
 *
 * Поддерживает 9-patch маски.
 */
class MaskTransformation(private val overlayBundle: OverlayBundle) : BaseGlideImageTransformation() {

    private val paint = Paint()

    init {
        paint.xfermode = PorterDuffXfermode(overlayBundle.mode)
    }

    override fun getId() = "ru.surfstudio.android.imageloader.transformations.MaskTransformation"

    override fun transform(
            context: Context,
            pool: BitmapPool,
            toTransform: Bitmap,
            outWidth: Int,
            outHeight: Int
    ): Bitmap? {
        val width = toTransform.width
        val height = toTransform.height

        val bitmap = pool.get(width, height, Bitmap.Config.ARGB_8888)
        bitmap.setHasAlpha(true)

        val mask = getMaskDrawable(context, overlayBundle.maskResId)

        val canvas = Canvas(bitmap)
        mask.setBounds(0, 0, width, height)
        mask.draw(canvas)
        canvas.drawBitmap(toTransform, 0f, 0f, paint)

        return bitmap
    }

    override fun hashCode() = getId().hashCode()

    override fun equals(other: Any?) = other is MaskTransformation

    @Suppress("DEPRECATION")
    private fun getMaskDrawable(context: Context, maskId: Int): Drawable {
        val drawable = if (SdkUtils.isAtLeastLollipop()) {
            context.getDrawable(maskId)
        } else {
            context.resources.getDrawable(maskId)
        }

        return drawable
                ?: throw IllegalArgumentException("MaskTransformation error / maskId is invalid")
    }

    /**
     * Конфигурационные данные для трансформации [MaskTransformation].
     */
    data class OverlayBundle(
            val isOverlay: Boolean = false,
            @DrawableRes val maskResId: Int = -1,
            val mode: PorterDuff.Mode = PorterDuff.Mode.SRC_IN
    )
}