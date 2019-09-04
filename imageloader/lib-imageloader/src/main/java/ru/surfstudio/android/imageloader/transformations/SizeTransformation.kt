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
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import ru.surfstudio.android.imageloader.data.ImageSizeManager

/**
 * Трансформатор, пережимающий изображение с учётом заданной максимальной высоты и ширины
 * без нарушение аспекта и искажения пропорций.
 */
class SizeTransformation(
        private val filterOnScale: Boolean = false,
        private val imageSizeManager: ImageSizeManager = ImageSizeManager()
) : BaseGlideImageTransformation() {

    override fun getId() = "ru.surfstudio.android.imageloader.transformations.SizeTransformation"

    override fun transform(
            context: Context,
            pool: BitmapPool,
            toTransform: Bitmap,
            outWidth: Int,
            outHeight: Int
    ): Bitmap? {
        if (!imageSizeManager.isMaxHeightSetUp() && !imageSizeManager.isMaxWidthSetUp()) {
            return toTransform
        }

        val originalWidth = toTransform.width
        val originalHeight = toTransform.height

        val widthFactor = if (!imageSizeManager.isMaxWidthSetUp()) {
            Float.MIN_VALUE
        } else {
            1.0f * originalWidth / imageSizeManager.maxWidth
        }
        val heightFactor = if (!imageSizeManager.isMaxHeightSetUp()) {
            Float.MIN_VALUE
        } else {
            1.0f * originalHeight / imageSizeManager.maxHeight
        }
        val scaleFactor = Math.max(heightFactor, widthFactor)
        val newHeight = (originalHeight / scaleFactor).toInt()
        val newWidth = (originalWidth / scaleFactor).toInt()
        return Bitmap.createScaledBitmap(toTransform, newWidth, newHeight, filterOnScale)
    }

    override fun hashCode() = getId().hashCode()

    override fun equals(other: Any?): Boolean {
        if (other is SizeTransformation) {
            return filterOnScale == other.filterOnScale && imageSizeManager == other.imageSizeManager
        }
        return false
    }
}