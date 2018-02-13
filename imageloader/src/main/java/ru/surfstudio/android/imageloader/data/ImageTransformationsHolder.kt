package ru.surfstudio.android.imageloader.data

import android.graphics.Bitmap
import com.bumptech.glide.load.Transformation
import ru.surfstudio.android.imageloader.transformations.SizeTransformation

/**
 * Пакет, хранящий все применяемые к изображению трансформации
 */
data class ImageTransformationsHolder(
        var imageSizeHolder: ImageSizeHolder = ImageSizeHolder()
) {

    private var transformations = arrayListOf<Transformation<Bitmap>>()   //список всех применяемых трансформаций

    /**
     * Подготовка всех требуемых трансформаций.
     */
    fun prepareTransformations() =
            transformations
                    .apply { add(SizeTransformation(imageSizeHolder = imageSizeHolder)) }
                    .toTypedArray()
}