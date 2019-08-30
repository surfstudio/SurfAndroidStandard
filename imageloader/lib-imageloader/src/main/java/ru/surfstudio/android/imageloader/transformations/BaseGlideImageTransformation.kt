/*
  Copyright (c) 2018-present, SurfStudio LLC.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package ru.surfstudio.android.imageloader.transformations

import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapResource
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.util.Util
import java.security.MessageDigest

/**
 * Базовый класс для всех трансформаций
 */
abstract class BaseGlideImageTransformation : Transformation<Bitmap> {

    abstract fun getId(): String

    protected abstract fun transform(
            context: Context,
            pool: BitmapPool,
            toTransform: Bitmap,
            outWidth: Int,
            outHeight: Int
    ): Bitmap?

    final override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(getIdBytes())
    }

    override fun transform(
            context: Context,
            resource: Resource<Bitmap>,
            outWidth: Int,
            outHeight: Int
    ): Resource<Bitmap> {
        if (!Util.isValidDimensions(outWidth, outHeight)) {
            throw IllegalArgumentException(
                    "Невозможно применить трансформацию с шириной: " + outWidth + " или высотой: " + outHeight
                            + " меньшими, или равными нуля, и не Target.SIZE_ORIGINAL")
        }
        val bitmapPool = Glide.get(context).bitmapPool
        val toTransform = resource.get()
        val targetWidth = if (outWidth == Target.SIZE_ORIGINAL) toTransform.width else outWidth
        val targetHeight = if (outHeight == Target.SIZE_ORIGINAL) toTransform.height else outHeight
        val transformed = transform(context, bitmapPool, toTransform, targetWidth, targetHeight)

        val result = if (toTransform == transformed) {
            resource
        } else {
            BitmapResource.obtain(transformed, bitmapPool)
        }

        return result ?: throw IllegalStateException("Невозможно применить трансформацию")
    }

    override fun hashCode() = getId().hashCode()

    override fun equals(other: Any?) = other is SizeTransformation

    private fun getIdBytes() = getId().byteInputStream().readBytes()
}