package ru.surfstudio.android.imageloader.transformations

import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest

/**
 * Базовый класс
 */
abstract class BaseImageTransformation : BitmapTransformation() {

    abstract fun getId(): String

    fun getIdBytes() = getId().byteInputStream().readBytes()

    final override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(getIdBytes())
    }

    override fun hashCode() = getId().hashCode()

    override fun equals(other: Any?) = other is SizeTransformation
}