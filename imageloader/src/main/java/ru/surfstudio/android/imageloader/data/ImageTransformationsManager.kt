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
package ru.surfstudio.android.imageloader.data

import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.load.Transformation
import ru.surfstudio.android.imageloader.transformations.*
import ru.surfstudio.android.imageloader.transformations.RoundedCornersTransformation.RoundedCornersBundle
import ru.surfstudio.android.imageloader.transformations.BlurTransformation.BlurBundle
import ru.surfstudio.android.imageloader.transformations.MaskTransformation.OverlayBundle
import ru.surfstudio.android.imageloader.transformations.TileTransformation.TileBundle

/**
 * Пакет, хранящий все применяемые к изображению трансформации
 */
data class ImageTransformationsManager(
        private val context: Context,
        var imageSizeManager: ImageSizeManager = ImageSizeManager(),
        var isCenterCrop: Boolean = false,
        var isCircle: Boolean = false,
        var roundedCornersBundle: RoundedCornersBundle = RoundedCornersBundle(),
        var blurBundle: BlurBundle = BlurBundle(),
        var overlayBundle: OverlayBundle = OverlayBundle(),
        var isDownsampled: Boolean = false,
        var sizeMultiplier: Float = 1f,
        var tileBundle: TileBundle = TileBundle()
) {

    private var transformations = arrayListOf<Transformation<Bitmap>>()   //список всех применяемых трансформаций

    /**
     * Подготовка всех требуемых трансформаций.
     */
    fun prepareTransformations() =
            transformations
                    .apply {
                        clear()
                        if (imageSizeManager.isMaxHeightSetUp() || imageSizeManager.isMaxWidthSetUp())
                            add(SizeTransformation(imageSizeManager = imageSizeManager))
                        if (tileBundle.isTiled)
                            add(TileTransformation(tileBundle))
                        if (isCenterCrop) add(CenterCropTransformation())
                        if (isCircle) add(CircleTransformation())
                        if (roundedCornersBundle.isRoundedCorners)
                            add(RoundedCornersTransformation(roundedCornersBundle))
                        if (blurBundle.isBlur)
                            add(BlurTransformation(blurBundle))
                        if (overlayBundle.isOverlay)
                            add(MaskTransformation(overlayBundle))
                    }
}