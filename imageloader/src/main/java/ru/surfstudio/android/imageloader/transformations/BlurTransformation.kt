package ru.surfstudio.android.imageloader.transformations

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool

/**
 * Эффект размытия изображения "Blur"
 */
class BlurTransformation(val context: Context,
                         private val bitmapPool: BitmapPool = Glide.get(context).bitmapPool,
                         private var blurBundle: BlurBundle = BlurBundle()) : BaseGlideImageTransformation() {

    override fun getId() = BlurTransformation::class.java.canonicalName.toString()

    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap? {

        val width = toTransform.width
        val height = toTransform.height
        val scaledWidth = width / blurBundle.downSampling
        val scaledHeight = height / blurBundle.downSampling

        var bitmap: Bitmap? = bitmapPool.get(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888)
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888)
        }

        val canvas = Canvas(bitmap!!)
        canvas.scale(1 / blurBundle.downSampling.toFloat(), 1 / blurBundle.downSampling.toFloat())
        val paint = Paint()
        paint.flags = Paint.FILTER_BITMAP_FLAG
        canvas.drawBitmap(toTransform, 0f, 0f, paint)
        return blur(context, bitmap, blurBundle.radiusPx)
    }

    override fun hashCode() = getId().hashCode()

    override fun equals(other: Any?) = other is BlurTransformation

    private fun blur(context: Context, bitmap: Bitmap, radiusPx: Int): Bitmap {
        var rs: RenderScript? = null
        try {
            rs = RenderScript.create(context)
            rs.messageHandler = RenderScript.RSMessageHandler()
            val input = Allocation.createFromBitmap(rs, bitmap, Allocation.MipmapControl.MIPMAP_NONE,
                    Allocation.USAGE_SCRIPT)
            val output = Allocation.createTyped(rs, input.type)
            val blur = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))

            blur.setInput(input)
            blur.setRadius(radiusPx.toFloat())
            blur.forEach(output)
            output.copyTo(bitmap)
        } finally {
            rs?.destroy()
        }
        return bitmap
    }

    /**
     * Конфигурационные данных для трансформации [BlurTransformation].
     */
    data class BlurBundle(val isBlur: Boolean = false,
                          val radiusPx: Int = 25,
                          val downSampling: Int = 1)
}