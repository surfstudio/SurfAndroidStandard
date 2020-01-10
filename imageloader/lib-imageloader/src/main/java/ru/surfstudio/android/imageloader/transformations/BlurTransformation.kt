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
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import ru.surfstudio.android.imageloader.util.BlurStrategy
import ru.surfstudio.android.imageloader.util.BlurUtil

/**
 * Эффект размытия изображения "Blur"
 */
class BlurTransformation(private var blurBundle: BlurBundle = BlurBundle()) : BaseGlideImageTransformation() {

    override fun getId() = "ru.surfstudio.android.imageloader.transformations.BlurTransformation"

    override fun transform(
            context: Context,
            pool: BitmapPool,
            toTransform: Bitmap,
            outWidth: Int,
            outHeight: Int
    ): Bitmap? {
        val width = toTransform.width
        val height = toTransform.height
        val sampling = blurBundle.downSampling
        val scaledWidth = width / sampling
        val scaledHeight = height / sampling

        var bitmap: Bitmap? = pool.get(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888)
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888)
        }

        val canvas = Canvas(bitmap!!)
        canvas.scale(1 / sampling.toFloat(), 1 / sampling.toFloat())
        val paint = Paint()
        paint.flags = Paint.FILTER_BITMAP_FLAG
        canvas.drawBitmap(toTransform, 0f, 0f, paint)
        return if (blurBundle.blurStrategy == BlurStrategy.STACK_BLUR) {
            BlurUtil.stackBlur(bitmap, blurBundle.radiusPx, true)
        } else {
            BlurUtil.renderScriptBlur(context, bitmap, blurBundle.radiusPx)
        }
    }

    override fun hashCode() = getId().hashCode()

    override fun equals(other: Any?) = other is BlurTransformation

    /**
     * Конфигурационные данных для трансформации [BlurTransformation].
     */
    data class BlurBundle(
            val isBlur: Boolean = false,
            val radiusPx: Int = 25,
            val downSampling: Int = 1,
            val blurStrategy: BlurStrategy = BlurStrategy.RENDER_SCRIPT
    )
}