package ru.surfstudio.android.imageloader.transformations

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.Shader
import android.graphics.drawable.BitmapDrawable
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool

/**
 * Трансформация, добавляющая эффект размножения картинки до ширины и высоты целевых
 */
class TileTransformation(
        val tileBundle: TileBundle
) : BaseGlideImageTransformation() {

    override fun getId(): String = "ru.surfstudio.android.imageloader.transformations.TileTransformation"

    override fun transform(
            context: Context,
            pool: BitmapPool,
            toTransform: Bitmap,
            outWidth: Int,
            outHeight: Int
    ): Bitmap? {
        val bitmapDrawable = BitmapDrawable(context.resources, toTransform)
        bitmapDrawable.setTileModeXY(tileBundle.tileMode, tileBundle.tileMode)
        bitmapDrawable.bounds = Rect(0, 0, outWidth, outHeight)
        val bitmap = pool.get(outWidth, outHeight, bitmapDrawable.bitmap.config)
        bitmapDrawable.draw(Canvas(bitmap))
        return bitmap
    }

    override fun hashCode() = getId().hashCode()

    override fun equals(other: Any?) = other is TileTransformation

    /**
     * Конфигурационные данные для трансформации [TileTransformation].
     */
    class TileBundle(
            val isTiled: Boolean = false,
            val tileMode: Shader.TileMode = Shader.TileMode.REPEAT
    )
}