package ru.surfstudio.android.imageloader.transformations

import android.graphics.Bitmap
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.TransformationUtils
import ru.surfstudio.android.imageloader.data.ImageSizeManager

/**
 * Трансформатор для скругления углов изображения.
 */
class RoundedCornerTransformation : BaseGlideImageTransformation() {

    override fun getId() = RoundedCornerTransformation::class.java.canonicalName.toString()

    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap =
            TransformationUtils.centerCrop(pool, toTransform, outWidth, outHeight)

    override fun hashCode() = getId().hashCode()

    override fun equals(other: Any?) = other is RoundedCornerTransformation

    /**
     * Угол для скругления.
     */
    enum class Corner {
        ALL,
        TOP, TOP_LEFT, TOP_RIGHT,
        BOTTOM, BOTTOM_LEFT, BOTTOM_RIGHT,
        LEFT, RIGHT,
        EXCEPT_TOP_LEFT, EXCEPT_TOP_RIGHT, EXCEPT_BOTTOM_LEFT, EXCEPT_BOTTOM_RIGHT,
        DIAGONAL_TOP_LEFT, DIAGONAL_TOP_RIGHT
    }
}