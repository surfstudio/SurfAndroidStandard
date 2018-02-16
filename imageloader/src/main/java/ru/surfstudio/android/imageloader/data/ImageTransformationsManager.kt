package ru.surfstudio.android.imageloader.data

import android.graphics.Bitmap
import com.bumptech.glide.load.Transformation
import ru.surfstudio.android.imageloader.transformations.SizeTransformation

/**
 * Пакет, хранящий все применяемые к изображению трансформации
 */
data class ImageTransformationsManager(
        var imageSizeManager: ImageSizeManager = ImageSizeManager()
) {

    private var transformations = arrayListOf<Transformation<Bitmap>>()   //список всех применяемых трансформаций

    /**
     * Подготовка всех требуемых трансформаций.
     */
    fun prepareTransformations() =
            transformations
                    .apply { add(SizeTransformation(imageSizeManager = imageSizeManager)) }
                    .toTypedArray()
}