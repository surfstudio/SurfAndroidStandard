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
package ru.surfstudio.android.imageloader.data

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.request.RequestOptions
import ru.surfstudio.android.imageloader.DEFAULT_DRAWABLE_URI
import ru.surfstudio.android.imageloader.util.applyTransformations

/**
 * Пакет со ссылками на все необходимые изображения и сервисными методами.
 */
data class ImageResourceManager(
        private val context: Context,
        private var imageTransformationsManager: ImageTransformationsManager = ImageTransformationsManager(context),
        /**
         * сетевая ссылка на изображение
         */
        var url: String = "",
        /**
         * заголовки, которые будут добавлены в запрос,
         * если указан [url]
         */
        var headers: Map<String, String> = mapOf(),
        /**
         * ссылка на drawable-ресурс
         */
        @DrawableRes
        var drawableResId: Int = DEFAULT_DRAWABLE_URI,
        /**
         * ссылка на drawable-ресурс при ошибке
         */
        @DrawableRes
        var errorResId: Int = DEFAULT_DRAWABLE_URI,
        /**
         * ссылка на drawable-ресурс плейсхолдера
         */
        @DrawableRes
        var previewResId: Int = DEFAULT_DRAWABLE_URI
) {

    var shouldTransformError = true

    var shouldTransformPreview = true

    var isHardwareConfigDisabled = false

    var isAnimationDisabled = false

    val isErrorSet: Boolean get() = errorResId != DEFAULT_DRAWABLE_URI

    val isPreviewSet: Boolean get() = previewResId != DEFAULT_DRAWABLE_URI


    /**
     * Метод, автоматически предоставляющий ссылку для загрузки изображения.
     *
     * Он способен определять, загружается ли изображение из сети или из ресурсов.
     */
    fun toLoad(): Any =
            if (isImageFromResourcesPresented()) {
                drawableResId
            } else {
                if (!isFileUrlSpecified() && hasValidHeaders()) {
                    GlideUrl(url, LazyHeaders.Builder().apply {
                        headers.forEach {
                            if (it.key.isNotEmpty()) {
                                addHeader(it.key, it.value)
                            }
                        }
                    }.build())
                } else {
                    url
                }
            }

    /**
     * Предоставлено ли изображение для ошибки
     */
    fun isErrorPresented() = errorResId != DEFAULT_DRAWABLE_URI

    /**
     * Подготовка заглушки для ошибки.
     *
     * К заглушке применяются все трансформации, применяемые и к исходному изображению.
     */
    fun prepareErrorDrawable() = prepareDrawable(errorResId, shouldTransformError)

    /**
     * Подготовка заглушки для плейсхолдера.
     *
     * К заглушке применяются все трансформации, применяемые и к исходному изображению.
     */
    fun preparePreviewDrawable() = prepareDrawable(previewResId, shouldTransformPreview)

    /**
     * Подготовка [Drawable] с применением всех трансформаций, применяемых и к исходному изображению.
     *
     * @param imageResId ссылка на drawable ресурс
     * @param shouldTransform необходимо ли применять к drawable трансформации для исходного изображения
     */
    private fun prepareDrawable(@DrawableRes imageResId: Int, shouldTransform: Boolean): RequestBuilder<Drawable> {
        val transformations = if (shouldTransform) {
            imageTransformationsManager.prepareTransformations()
        } else {
            emptyList<Transformation<Bitmap>>()
        }

        return Glide.with(context)
                .load(imageResId)
                .apply(RequestOptions()
                        .applyTransformations(transformations)
                )
    }

    /**
     * Загружается ли изображение из res/drawable
     */
    private fun isImageFromResourcesPresented() = drawableResId != DEFAULT_DRAWABLE_URI && drawableResId != 0

    private fun hasValidHeaders() = headers.entries.find {
        it.key.isNotEmpty()
    } != null

    /**
     * @return true если данная урла является файловой
     */
    private fun isFileUrlSpecified(): Boolean {
        if (url.isEmpty()) {
            return false
        }
        val url = Uri.parse(url)
        with(url.scheme) {
            return isNullOrEmpty() || this == ContentResolver.SCHEME_FILE
        }
    }
}