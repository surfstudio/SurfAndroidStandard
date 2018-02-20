package ru.surfstudio.android.imageloader.data

import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.load.Transformation
import ru.surfstudio.android.imageloader.transformations.*
import ru.surfstudio.android.imageloader.transformations.RoundedCornersTransformation.RoundedCornersBundle
import ru.surfstudio.android.imageloader.transformations.BlurTransformation.BlurBundle
import ru.surfstudio.android.imageloader.transformations.MaskTransformation.OverlayBundle

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
        var overlayBundle: OverlayBundle = OverlayBundle()
) {

    private var transformations = arrayListOf<Transformation<Bitmap>>()   //список всех применяемых трансформаций

    /**
     * Подготовка всех требуемых трансформаций.
     */
    fun prepareTransformations() =
            transformations
                    .apply {
                        add(SizeTransformation(imageSizeManager = imageSizeManager))
                        if (isCenterCrop) add(CenterCropTransformation())
                        if (isCircle) add(CircleTransformation())
                        if (roundedCornersBundle.isRoundedCorners)
                            add(RoundedCornersTransformation(context, roundedCornersBundle = roundedCornersBundle))
                        if (blurBundle.isBlur)
                            add(BlurTransformation(context, blurBundle = blurBundle))
                        if (overlayBundle.isOverlay)
                            add(MaskTransformation(context, overlayBundle))
                    }
                    .toTypedArray()
}