package ru.surfstudio.android.imageloader.transformations

import android.graphics.Bitmap
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest

/**
 * Трансформатор, пережимающий изображение с учётом заданной максимальной высоты и ширины
 * без нарушение аспекта и искажения пропорций.
 */
abstract class BaseImageTransformation : BitmapTransformation() {

    private val ID = this::class.java.canonicalName.toString()
    private val ID_BYTES = ID.toByte()

    final override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(ID_BYTES)
    }

    override fun hashCode() = ID.hashCode()
}