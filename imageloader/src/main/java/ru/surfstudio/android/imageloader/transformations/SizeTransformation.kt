package ru.surfstudio.android.imageloader.transformations

import android.graphics.Bitmap
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import ru.surfstudio.android.imageloader.data.ImageSizeManager

/**
 * Трансформатор, пережимающий изображение с учётом заданной максимальной высоты и ширины
 * без нарушение аспекта и искажения пропорций.
 */
class SizeTransformation(private val filterOnScale: Boolean = true,
                         private val imageSizeManager: ImageSizeManager = ImageSizeManager()
) : BaseGlideImageTransformation() {

    override fun getId() = SizeTransformation::class.java.canonicalName.toString()

    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
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

    override fun hashCode(): Int {
        return getId().hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (other is SizeTransformation) {
            return filterOnScale == other.filterOnScale && imageSizeManager == other.imageSizeManager
        }
        return false
    }
}