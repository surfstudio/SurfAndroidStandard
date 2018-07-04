package ru.surfstudio.android.imageloader.transformations

import android.content.Context
import android.graphics.RectF
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import android.graphics.Bitmap
import android.graphics.Canvas
import android.support.annotation.NonNull
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest

class CropTransformation constructor(private var width: Int, private var height: Int, var cropType: CropType = CropType.CENTER) : BaseGlideImageTransformation() {
    override fun getId() = CropTransformation::class.java.canonicalName.toString()

    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
        width = if (width == 0) toTransform.width else width
        height = if (height == 0) toTransform.height else height

        val config = if (toTransform.config != null) toTransform.config else Bitmap.Config.ARGB_8888
        val bitmap = pool.get(width, height, config)

        bitmap.setHasAlpha(true)

        val scaleX = width.toFloat() / toTransform.width
        val scaleY = height.toFloat() / toTransform.height
        val scale = Math.max(scaleX, scaleY)

        val scaledWidth = scale * toTransform.width
        val scaledHeight = scale * toTransform.height
        val left = (width - scaledWidth) / 2
        val top = getTop(scaledHeight)
        val targetRect = RectF(left, top, left + scaledWidth, top + scaledHeight)

        val canvas = Canvas(bitmap)
        canvas.drawBitmap(toTransform, null, targetRect, null)

        return bitmap
    }
    enum class CropType {
        TOP,
        CENTER,
        BOTTOM
    }

    private fun getTop(scaledHeight: Float): Float {
        when (cropType) {
            CropTransformation.CropType.TOP -> return 0f
            CropTransformation.CropType.CENTER -> return (height - scaledHeight) / 2
            CropTransformation.CropType.BOTTOM -> return height - scaledHeight
            else -> return 0f
        }
    }

    override fun toString(): String {
        return "CropTransformation(width=$width, height=$height, cropType=$cropType)"
    }

    companion object {

        private val VERSION = 1
        private val ID = "jp.wasabeef.glide.transformations.CropTransformation.$VERSION"
    }
}