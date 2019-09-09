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
import com.bumptech.glide.load.resource.bitmap.TransformationUtils

/**
 * Масштабирование изображения таким образом, чтобы либо ширина изображения соответствовала ширине
 * виджета, а высота изображения была больше высоты виджета, либо наоборот. Возникающие излишки
 * изображения обрезаются.
 */
class CenterCropTransformation : BaseGlideImageTransformation() {

    override fun getId() = "ru.surfstudio.android.imageloader.transformations.CenterCropTransformation"

    override fun transform(
            context: Context,
            pool: BitmapPool,
            toTransform: Bitmap,
            outWidth: Int,
            outHeight: Int
    ): Bitmap? {
        return TransformationUtils.centerCrop(pool, toTransform, outWidth, outHeight)
    }

    override fun hashCode() = getId().hashCode()

    override fun equals(other: Any?) = other is CenterCropTransformation
}