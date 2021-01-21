package ru.surfstudio.android.imageloader.transformations

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.RectF
import androidx.annotation.NonNull
import com.bumptech.glide.load.Key
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest

/**
 * Обрезка изображения с возможностью задать, где нужно обрезать картинку
 */
class CropTransformation constructor(private val cropBundle: CropBundle = CropBundle()) : BitmapTransformation() {

    private val cropType = CropType.CENTER

    enum class CropType {
        TOP,
        CENTER,
        BOTTOM
    }

    companion object {
        private val VERSION = 1
        private val ID = "jp.wasabeef.glide.transformations.CropTransformation.$VERSION"
    }

    override fun transform(@NonNull pool: BitmapPool, @NonNull toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
        cropBundle.width = if (cropBundle.width == 0) toTransform.width else cropBundle.width
        cropBundle.height = if (cropBundle.height == 0) toTransform.height else cropBundle.height

        val config = if (toTransform.config != null) toTransform.config else Bitmap.Config.ARGB_8888
        val bitmap = pool.get(cropBundle.width, cropBundle.height, config)

        bitmap.setHasAlpha(true)

        val scaleX = cropBundle.width.toFloat() / toTransform.width
        val scaleY = cropBundle.height.toFloat() / toTransform.height
        val scale = Math.max(scaleX, scaleY)

        val scaledWidth = scale * toTransform.width
        val scaledHeight = scale * toTransform.height
        val left = (cropBundle.width - scaledWidth) / 2
        val top = getTop(scaledHeight)
        val targetRect = RectF(left, top, left + scaledWidth, top + scaledHeight)

        val canvas = Canvas(bitmap)
        canvas.drawBitmap(toTransform, null, targetRect, null)

        return bitmap
    }

    private fun getTop(scaledHeight: Float): Float {
        when (cropType) {
            CropTransformation.CropType.TOP -> return 0f
            CropTransformation.CropType.CENTER -> return (cropBundle.height - scaledHeight) / 2
            CropTransformation.CropType.BOTTOM -> return cropBundle.height - scaledHeight
            else -> return 0f
        }
    }

    override fun toString(): String {
        return "CropTransformation(width=${cropBundle.width}, height=$cropBundle.height, cropType=$cropType)"
    }

    override fun equals(o: Any?): Boolean {
        return o is CropTransformation &&
                o.cropBundle.width == cropBundle.width &&
                o.cropBundle.height == cropBundle.height &&
                o.cropType == cropType
    }

    override fun hashCode(): Int {
        return ID.hashCode() + cropBundle.width * 100000 + cropBundle.height * 1000 + cropType.ordinal * 10
    }

    override fun updateDiskCacheKey(@NonNull messageDigest: MessageDigest) {
        messageDigest.update((ID + cropBundle.width + cropBundle.height + cropType).toByteArray(Key.CHARSET))
    }

    /**
     * Конфигурационные данных для трансформации [CropTransformation].
     */
    data class CropBundle(
            var isCustomCrop: Boolean = false,
            var width: Int = 0,
            var height: Int = 0,
            val cropType: CropTransformation.CropType = CropTransformation.CropType.CENTER
    )
}
