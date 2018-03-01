package ru.surfstudio.android.imageloader.transformations

import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest

/**
 * Базовый класс для всех трансформаций
 */
abstract class BaseGlideImageTransformation : BitmapTransformation() {

    abstract fun getId(): String

    final override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(getIdBytes())
    }

    override fun hashCode() = getId().hashCode()

    override fun equals(other: Any?) = other is SizeTransformation

    private fun getIdBytes() = getId().byteInputStream().readBytes()
}