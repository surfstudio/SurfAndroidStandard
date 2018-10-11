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

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.request.RequestOptions
import ru.surfstudio.android.imageloader.DEFAULT_DRAWABLE_URI
import ru.surfstudio.android.imageloader.applyTransformations

/**
 * Пакет со ссылками на все необходимые изображения и сервисными методами.
 */
data class ImageResourceManager(
        private val context: Context,
        private var imageTransformationsManager: ImageTransformationsManager = ImageTransformationsManager(context),
        var url: String = "",                           //сетевая ссылка на изображение
        @DrawableRes
        var drawableResId: Int = DEFAULT_DRAWABLE_URI,  //ссылка на drawable-ресурс
        @DrawableRes
        var errorResId: Int = DEFAULT_DRAWABLE_URI,     //ссылка на drawable-ресурс при ошибке
        @DrawableRes
        var previewResId: Int = DEFAULT_DRAWABLE_URI    //ссылка на drawable-ресурс плейсхолдера
) {

    /**
     * Метод, автоматически предоставляющий ссылку для загрузки изображения.
     *
     * Он способен определять, загружается ли изображение из сети или из ресурсов.
     */
    fun toLoad(): Any =
            if (isImageFromResourcesPresented()) {
                drawableResId
            } else {
                url
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
    fun prepareErrorDrawable() = prepareDrawable(errorResId)

    /**
     * Подготовка заглушки для плейсхолдера.
     *
     * К заглушке применяются все трансформации, применяемые и к исходному изображению.
     */
    fun preparePreviewDrawable() = prepareDrawable(previewResId)


    /**
     * Подготовка [Drawable] с применением всех трансформаций, применяемых и к исходному изображению.
     *
     * @param imageResId ссылка на drawable ресурс
     */
    private fun prepareDrawable(@DrawableRes imageResId: Int): RequestBuilder<Drawable> {
        return Glide.with(context)
                .load(imageResId)
                .apply(RequestOptions()
                        .applyTransformations(imageTransformationsManager)
                )
    }

    /**
     * Загружается ли изображение из res/drawable
     */
    private fun isImageFromResourcesPresented() = drawableResId != DEFAULT_DRAWABLE_URI
}