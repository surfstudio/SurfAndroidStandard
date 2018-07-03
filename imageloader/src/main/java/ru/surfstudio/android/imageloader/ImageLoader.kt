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
package ru.surfstudio.android.imageloader

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.support.annotation.WorkerThread
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.target.ViewTarget
import com.bumptech.glide.request.transition.Transition
import ru.surfstudio.android.imageloader.data.*
import ru.surfstudio.android.imageloader.transformations.BlurTransformation.BlurBundle
import ru.surfstudio.android.imageloader.transformations.MaskTransformation.OverlayBundle
import ru.surfstudio.android.imageloader.transformations.RoundedCornersTransformation.CornerType
import ru.surfstudio.android.imageloader.transformations.RoundedCornersTransformation.RoundedCornersBundle
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.android.utilktx.util.DrawableUtil
import java.util.concurrent.ExecutionException

/**
 * Загрузчик изображений.
 *
 * Является надстройкой над библиотекой Glide.
 */
class ImageLoader(private val context: Context) : ImageLoaderInterface {

    private var imageCacheManager: ImageCacheManager = ImageCacheManager()
    private var imageTransformationsManager: ImageTransformationsManager =
            ImageTransformationsManager(context)
    private var imageResourceManager: ImageResourceManager =
            ImageResourceManager(context, imageTransformationsManager)
    private var imageTargetManager: ImageTargetManager =
            ImageTargetManager(context, imageResourceManager)
    private var imageTagManager: ImageTagManager =
            ImageTagManager(imageTargetManager, imageResourceManager)

    private var onImageLoadedLambda: ((drawable: Drawable) -> (Unit))? = null
    private var onImageLoadErrorLambda: ((throwable: Throwable) -> (Unit))? = null

    private val glideDownloadListener = object : RequestListener<Drawable> {
        override fun onLoadFailed(e: GlideException?,
                                  model: Any?,
                                  target: Target<Drawable>?,
                                  isFirstResource: Boolean) = false.apply {
            imageTagManager.setTag(null)
            e?.let { onImageLoadErrorLambda?.invoke(it) }
        }

        override fun onResourceReady(resource: Drawable,
                                     model: Any?,
                                     target: Target<Drawable>?,
                                     dataSource: DataSource?,
                                     isFirstResource: Boolean): Boolean =
                false.apply { onImageLoadedLambda?.invoke(resource) }
    }

    companion object {
        fun with(context: Context) = ImageLoader(context)
    }

    /**
     * Загрузка изображения из сети
     *
     * @param url сетевая ссылка на изображение
     */
    @Throws(IllegalArgumentException::class)
    override fun url(url: String) =
            apply {
                this.imageResourceManager.url = url
            }

    /**
     * Загрузка изображения из ресурсов
     *
     * @param drawableResId ссылка на ресурс из папки res/drawable
     */
    override fun url(@DrawableRes drawableResId: Int) =
            apply { this.imageResourceManager.drawableResId = drawableResId }

    /**
     * Указание графического ресурса, отображаемого в качестве плейсхолдера
     *
     * @param drawableResId ссылка на ресурс из папки res/drawable
     */
    override fun preview(@DrawableRes drawableResId: Int) =
            apply { this.imageResourceManager.previewResId = drawableResId }

    /**
     * Указание графического ресурса, отображаемого в случае ошибки загрузки
     *
     * @param drawableResId ссылка на ресурс из папки res/drawable
     */
    override fun error(@DrawableRes drawableResId: Int) =
            apply { this.imageResourceManager.errorResId = drawableResId }

    /**
     * Установка лямбды для отслеживания загрузки изображения
     *
     * @param lambda лямбда, возвращающая загруженный [Drawable]
     */
    override fun listener(lambda: ((drawable: Drawable) -> (Unit))) =
            apply { this.onImageLoadedLambda = lambda }

    /**
     * Установка лямбды для отслеживания ошибки при загрузке изображения
     *
     * @param lambda лямбда, возвращающая ошибку [Throwable]
     */
    override fun errorListener(lambda: ((throwable: Throwable) -> (Unit))) =
            apply { this.onImageLoadErrorLambda = lambda }

    /**
     * Указание политики кэширования.
     * Метод предоставляет возможность отключить кэширование загруженных изображений в памяти и на диске.
     *
     * В данной реализации кэшируется только финальное изображение (прошедшее необходимую постобработку и трансформации).
     * @param skipCache true - игнорировать кэш в памяти и на диске, false - использовать кэш в памяти и на диске
     */
    override fun skipCache(skipCache: Boolean) =
            apply { this.imageCacheManager.skipCache = skipCache }

    /**
     * Установка максимальной ширины изображения в px.
     *
     * Необходима для пережатия изображения без искажения пропорций.
     */
    override fun maxWidth(maxWidth: Int) =
            apply { this.imageTransformationsManager.imageSizeManager.maxWidth = maxWidth }

    /**
     * Установка максимальной высоты изображения в px.
     *
     * Необходима для пережатия изображения без искажения пропорций.
     */
    override fun maxHeight(maxHeight: Int) =
            apply { this.imageTransformationsManager.imageSizeManager.maxHeight = maxHeight }

    /**
     * Масштабирование изображения по размеру виджета с обрезкой излишков.
     *
     * @param isCrop флаг активации трансформации
     */
    override fun centerCrop(isCrop: Boolean) =
            apply { this.imageTransformationsManager.isCenterCrop = isCrop }

    /**
     * Преобразование прямоугольного изображения в круглое.
     *
     * @param isCircle флаг активации трансформации
     */
    override fun circle(isCircle: Boolean) =
            apply { imageTransformationsManager.isCircle = isCircle }

    /**
     * Скругление углов у прямоугольного изображения.
     *
     * @param isRoundedCorners флаг активации трансформации
     * @param radiusPx радиус скругления в px
     * @param marginPx величина отступа в px
     * @param cornerType конфигурация скругляемых углов
     */
    override fun roundedCorners(isRoundedCorners: Boolean, radiusPx: Int, marginPx: Int, cornerType: CornerType) =
            also {
                imageTransformationsManager.roundedCornersBundle =
                        RoundedCornersBundle(isRoundedCorners, cornerType, radiusPx, marginPx)
            }

    /**
     * Эффект размытия изображения "Blur".
     *
     * @param isBlur флаг активации трансформации
     * @param blurRadiusPx радиус размытия
     * @param blurDownSampling уровень принудительного понижения качества разрешения изображения
     */
    override fun blur(isBlur: Boolean, blurRadiusPx: Int, blurDownSampling: Int) =
            also {
                imageTransformationsManager.blurBundle =
                        BlurBundle(isBlur, blurRadiusPx, blurDownSampling)
            }

    /**
     * Наложение маски на изображение с поддержкой 9-patch маски.
     *
     * @param isOverlay флаг активации трансформации
     * @param maskResId ссылка на ресурс изображения маски из папки res/drawable
     */
    override fun mask(isOverlay: Boolean, @DrawableRes maskResId: Int) =
            also {
                imageTransformationsManager.overlayBundle = OverlayBundle(isOverlay, maskResId)
            }

    override fun sizeMultiplier(sizeMultiplier: Float): ImageLoaderInterface =
            also {
                imageTransformationsManager.sizeMultiplier = sizeMultiplier
            }

    /**
     * Указание целевой [View]
     *
     * @param view экземпляр [View] для загрузки изображения
     */
    override fun into(view: View) {
        this.imageTargetManager.targetView = view

        if (imageTagManager.isTagUsed()) return

        imageTagManager.setTag(imageResourceManager.url)

        performLoad(view)
    }

    /**
     * Загрузка изображения в [SimpleTarget].
     *
     * @param simpleTarget обработчик загрузки изображения.
     */
    fun into(simpleTarget: SimpleTarget<Drawable>) {
        into(
                { resource, transition -> simpleTarget.onResourceReady(resource, transition) },
                {
                    it?.let {
                        simpleTarget.onResourceReady(it, null)
                    }
                }
        )
    }

    /**
     * Загрузка изображения в [SimpleTarget]
     *
     * @param resourceReadyLambda колбек в случае успеха
     * @param loadFailedLambda колбек при ошибке
     */
    fun into(
            resourceReadyLambda: (resource: Drawable, transition: Transition<in Drawable>?) -> Unit,
            loadFailedLambda: (errorDrawable: Drawable?) -> Unit
    ) {
        buildRequest().into(object : SimpleTarget<Drawable>() {
            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                resourceReadyLambda(resource, transition)
            }

            override fun onLoadFailed(errorDrawable: Drawable?) {
                super.onLoadFailed(errorDrawable)
                loadFailedLambda(errorDrawable)
            }
        })
    }

    /**
     * Получение исходника изображения в формате [Bitmap].
     * Кейс использования - загрузка изображения на уровне интерактора для отправки на сервер.
     * Без отображения на UI.
     * Для отображения на UI использовать [into]
     *
     * Запрос происходит в UI-потоке.
     */
    @WorkerThread
    override fun get(): Bitmap? {
        var result: Bitmap?
        try {
            result = Glide.with(context)
                    .asBitmap()
                    .load(imageResourceManager.toLoad())
                    .apply(
                            RequestOptions()
                                    .diskCacheStrategy(if (imageCacheManager.skipCache) DiskCacheStrategy.NONE else DiskCacheStrategy.ALL)
                                    .skipMemoryCache(imageCacheManager.skipCache)
                                    .transforms(*imageTransformationsManager.prepareTransformations())
                    )
                    .submit()
                    .get()
        } catch (e: Exception) {
            when (e) {
                is InterruptedException, is ExecutionException -> {
                    if (!imageResourceManager.isErrorPresented()) {
                        Logger.e("ImageLoader.get() / Ошибка загрузки изображения")
                        throw IllegalStateException("Ошибка загрузки изображения")
                    }
                    onImageLoadErrorLambda?.invoke(e)
                    result = DrawableUtil.getBitmapFromRes(context, imageResourceManager.errorResId)
                }
                else -> throw e
            }
        }
        return result
    }


    /**
     * Формирование запроса на загрузку изображения.
     */
    private fun buildRequest(): RequestBuilder<Drawable> = Glide.with(context)
            .load(imageResourceManager.toLoad())
            .error(imageResourceManager.prepareErrorDrawable())
            .thumbnail(imageResourceManager.preparePreviewDrawable())
            .apply(
                    RequestOptions()
                            /*.diskCacheStrategy(if (imageCacheManager.skipCache) DiskCacheStrategy.NONE else DiskCacheStrategy.ALL)
                            .skipMemoryCache(imageCacheManager.skipCache)
                            .downsample(DownsampleStrategy.AT_MOST)
                            .sizeMultiplier(imageTransformationsManager.sizeMultiplier)*/
                            .transforms(*imageTransformationsManager.prepareTransformations())
            )
            .listener(glideDownloadListener)

    /**
     * Загрузка изображения в целевую [View].
     *
     * @param view целевая [View]
     */
    private fun performLoad(view: View) {
        if (view is ImageView) {
            buildRequest().into(view)
        } else {
            buildRequest().into(object : ViewTarget<View, Drawable>(view) {

                override fun onLoadStarted(placeholder: Drawable?) {
                    super.onLoadStarted(placeholder)
                    view.background = placeholder
                }

                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                    view.background = resource
                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    super.onLoadFailed(errorDrawable)
                    view.background = errorDrawable
                }
            })
        }
    }

}