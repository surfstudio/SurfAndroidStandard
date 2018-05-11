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