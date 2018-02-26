package ru.surfstudio.android.imageloader.transformations

import android.graphics.Bitmap
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.TransformationUtils
import ru.surfstudio.android.imageloader.data.ImageSizeManager

/**
 * Масштабирование изображения таким образом, чтобы либо ширина изображения соответствовала ширине
 * виджета, а высота изображения была больше высоты виджета, либо наоборот. Возникающие излишки
 * изображения обрезаются.
 */
class CenterCropTransformation : BaseGlideImageTransformation() {

    override fun getId() = CenterCropTransformation::class.java.canonicalName.toString()

    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap =
            TransformationUtils.centerCrop(pool, toTransform, outWidth, outHeight)

    override fun hashCode() = getId().hashCode()

    override fun equals(other: Any?) = other is CenterCropTransformation
}