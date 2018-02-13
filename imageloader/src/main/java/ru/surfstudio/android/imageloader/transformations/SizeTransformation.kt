package ru.surfstudio.android.imageloader.transformations

import android.graphics.Bitmap
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import ru.surfstudio.android.imageloader.data.ImageSizeHolder


/**
 * Трансформатор, пережимающий изображение с учётом заданной максимальной высоты и ширины
 * без нарушение аспекта и искажения пропорций.
 */
class SizeTransformation(private val filterOnScale: Boolean = true,
                         private val imageSizeHolder: ImageSizeHolder = ImageSizeHolder()
) : BaseImageTransformation() {

    override fun getId() = SizeTransformation::class.java.canonicalName.toString()

    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
        if (!imageSizeHolder.isMaxHeightSetUp() && !imageSizeHolder.isMaxWidthSetUp()) {
            return toTransform
        }

        val originalWidth = toTransform.width
        val originalHeight = toTransform.height

        val widthFactor = if (!imageSizeHolder.isMaxWidthSetUp()) {
            Float.MIN_VALUE
        } else {
            1.0f * originalWidth / imageSizeHolder.maxWidth
        }
        val heightFactor = if (!imageSizeHolder.isMaxHeightSetUp()) {
            Float.MIN_VALUE
        } else {
            1.0f * originalHeight / imageSizeHolder.maxHeight
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
            return filterOnScale == other.filterOnScale && imageSizeHolder == other.imageSizeHolder
        }
        return false
    }
}