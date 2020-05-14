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
import android.graphics.PorterDuff
import android.graphics.Shader
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.FloatRange
import androidx.annotation.WorkerThread
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.bumptech.glide.request.transition.Transition
import com.bumptech.glide.signature.ObjectKey
import ru.surfstudio.android.imageloader.data.*
import ru.surfstudio.android.imageloader.transformations.BlurTransformation.BlurBundle
import ru.surfstudio.android.imageloader.transformations.MaskTransformation.OverlayBundle
import ru.surfstudio.android.imageloader.transformations.RoundedCornersTransformation.CornerType
import ru.surfstudio.android.imageloader.transformations.RoundedCornersTransformation.RoundedCornersBundle
import ru.surfstudio.android.imageloader.transformations.TileTransformation.TileBundle
import ru.surfstudio.android.imageloader.util.*
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.android.utilktx.util.DrawableUtil
import java.util.concurrent.ExecutionException

@Suppress("MemberVisibilityCanBePrivate")
/**
 * Загрузчик изображений.
 *
 * Является надстройкой над библиотекой Glide.
 *
 * При загрузке изображений не следует использовать [View] с длиной и шириной wrap_content.
 * Это может повлечь за собой загрузку изображения с размерами экрана смартфона,
 * а так же возникновение багов (например, перестанут применяться трансформации).
 */
class ImageLoader(private val context: Context) : ImageLoaderInterface {

    private var imageCacheManager = ImageCacheManager()
    private var imageTransformationsManager = ImageTransformationsManager(context)
    private var imageResourceManager = ImageResourceManager(context, imageTransformationsManager)
    private var imageTargetManager = ImageTargetManager(context, imageResourceManager)
    private var imageSignatureManager = SignatureManager()
    private var imageTagManager = ImageTagManager(imageTargetManager, imageResourceManager, imageSignatureManager)
    private var imageTransitionManager = ImageTransitionManager()

    private var onImageLoadedLambda: ((drawable: Drawable, imageSource: ImageSource?) -> (Unit))? = null
    private var onImageLoadErrorLambda: ((throwable: Throwable) -> (Unit))? = null

    private val glideDownloadListener = object : RequestListener<Drawable> {
        override fun onLoadFailed(e: GlideException?,
                                  model: Any?,
                                  target: Target<Drawable>?,
                                  isFirstResource: Boolean) = false.apply {
            e?.let { onImageLoadErrorLambda?.invoke(it) }
        }

        override fun onResourceReady(resource: Drawable,
                                     model: Any?,
                                     target: Target<Drawable>?,
                                     dataSource: DataSource?,
                                     isFirstResource: Boolean): Boolean {
            val imageSource = dataSource?.toImageSource()
            imageCacheManager.imageSource = imageSource
            onImageLoadedLambda?.invoke(resource, imageSource)
            return false
        }
    }

    companion object {
        fun with(context: Context) = ImageLoader(context)
    }

    override fun url(url: String) = url(url, emptyMap())

    @Throws(IllegalArgumentException::class)
    override fun url(url: String, headers: Map<String, String>) =
            apply {
                this.imageResourceManager.url = url
                this.imageResourceManager.headers = headers
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
    override fun preview(@DrawableRes drawableResId: Int, shouldTransformPreview: Boolean) =
            apply {
                this.imageResourceManager.previewResId = drawableResId
                this.imageResourceManager.shouldTransformPreview = shouldTransformPreview
            }

    /**
     * Указание графического ресурса, отображаемого в случае ошибки загрузки
     *
     * @param drawableResId ссылка на ресурс из папки res/drawable
     */
    override fun error(@DrawableRes drawableResId: Int, shouldTransformError: Boolean) =
            apply {
                this.imageResourceManager.errorResId = drawableResId
                this.imageResourceManager.shouldTransformError = shouldTransformError
            }

    /**
     * Установка лямбды для отслеживания загрузки изображения
     *
     * @param lambda лямбда, возвращающая загруженный [Drawable] и [ImageSource], указывающий откуда он был загружен
     */
    override fun listenerWithSource(lambda: ((drawable: Drawable, imageSource: ImageSource?) -> (Unit))) =
            apply { this.onImageLoadedLambda = lambda }

    /**
     * Установка лямбды для отслеживания загрузки изображения
     *
     * @param lambda лямбда, возвращающая загруженный [Drawable]
     */
    override fun listener(lambda: ((drawable: Drawable) -> (Unit))) =
            apply { this.onImageLoadedLambda = { drawable, _ -> lambda(drawable) } }

    /**
     * Установка лямбды для отслеживания ошибки при загрузке изображения
     *
     * @param lambda лямбда, возвращающая ошибку [Throwable]
     */
    override fun errorListener(lambda: ((throwable: Throwable) -> (Unit))) =
            apply { this.onImageLoadErrorLambda = lambda }

    /**
     * Указание политики кэширования.
     * Метод предоставляет возможность настроить кеширование загруженных изображений на диске.
     *
     * @param cacheStrategy необходимая стратегия кеширования
     */
    override fun cacheStrategy(cacheStrategy: CacheStrategy): ImageLoaderInterface =
            apply { this.imageCacheManager.cacheStrategy = cacheStrategy }

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
     * @param blurStrategy стратегия размытия
     */
    override fun blur(isBlur: Boolean, blurRadiusPx: Int, blurDownSampling: Int, blurStrategy: BlurStrategy) =
            also {
                imageTransformationsManager.blurBundle =
                        BlurBundle(isBlur, blurRadiusPx, blurDownSampling, blurStrategy)
            }

    /**
     * Наложение маски на изображение с поддержкой 9-patch маски.
     *
     * @param isOverlay флаг активации трансформации
     * @param maskResId ссылка на ресурс изображения маски из папки res/drawable
     */
    override fun mask(isOverlay: Boolean, @DrawableRes maskResId: Int, overlayMode: PorterDuff.Mode) =
            also {
                imageTransformationsManager.overlayBundle = OverlayBundle(isOverlay, maskResId, overlayMode)
            }

    /**
     * Указание степени сжатия изображения.
     * Применяется для сжатия больших изображений во избежание переполнения памяти на устройстве.
     *
     * @param value значение множителя сжатия в диапазоне от 0 до 1
     * (0 - максимальное сжатие, 1 - минимальное сжатие)
     */
    override fun downsamplingMultiplier(@FloatRange(from = 0.0, to = 1.0) value: Float): ImageLoaderInterface =
            also {
                imageTransformationsManager.isDownsampled = true
                imageTransformationsManager.sizeMultiplier = value
            }

    /**
     * Добавление перехода с растворением между изображениями.
     *
     * @param duration продолжительность перехода (в мс)
     * @param hidePreviousImage заставляет Glide скрыть предыдущее изображение,
     * а не просто нарисовать следующее поверх см. документацию https://clck.ru/FVpbQ
     */
    override fun crossFade(duration: Int, hidePreviousImage: Boolean): ImageLoaderInterface =
            also {
                val factory = DrawableCrossFadeFactory.Builder(duration)
                        .setCrossFadeEnabled(hidePreviousImage)
                        .build()

                imageTransitionManager.imageTransitionOptions =
                        DrawableTransitionOptions().crossFade(factory)
            }

    /**
     * Размножения изображения для соответствия его размеров размерам View
     *
     * @param isTiled должно ли быть изображение размножено
     * @param tileMode тип размножения
     */
    override fun tile(isTiled: Boolean, tileMode: Shader.TileMode) = apply {
        imageTransformationsManager.tileBundle = TileBundle(isTiled, tileMode)
    }

    /**
     * Принудительная вставка изображения во вью
     * Необходимо в случае, если ссылка на изображение остаётся неизменной, а сама картинка меняется
     */
    override fun force() =
            apply { this.imageTagManager.force = true }


    override fun signature(signature: Any): ImageLoaderInterface =
            apply {
                this.imageSignatureManager.signature = signature
            }

    override fun disableHardwareConfig(): ImageLoaderInterface =
            apply {
                imageResourceManager.isHardwareConfigDisabled = true
            }

    override fun dontAnimate(): ImageLoaderInterface =
            apply {
                imageResourceManager.isAnimationDisabled = true
            }

    /**
     * Получение исходника изображения в формате [Bitmap].
     * Кейс использования - загрузка изображения на уровне интерактора для отправки на сервер.
     * Без отображения на UI.
     * Для отображения на UI использовать [into]
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
                                    .applyTransformations(imageTransformationsManager.prepareTransformations())
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
     * Указание целевой [View]
     *
     * @param view экземпляр [View] для загрузки изображения
     */
    override fun into(view: View) {
        this.imageTargetManager.targetView = view

        performLoad(view)
    }

    /**
     * Загрузка изображения с использованием Listener'ов и указанием целевой [View]
     *
     * @param view экземпляр view, используется для управления жизненным циклом
     * @param onErrorLambda лямбда, вызываемая при ошибке загрузки изображения.
     * @param onCompleteLambda лямбда, вызываемая при успешной загрузке изображения
     * @param onClearMemoryLambda лямбда, вызываемая, когда view может быть очищена. В ней следует
     * производить операции по дополнительному освобождению памяти.
     */
    override fun into(
            view: View,
            onErrorLambda: ((errorDrawable: Drawable?) -> Unit)?,
            onCompleteLambda: ((resource: Drawable?, imageSource: ImageSource?) -> Unit)?,
            onClearMemoryLambda: ((placeholder: Drawable?) -> Unit)?
    ) {
        into(
                view,
                onErrorLambda,
                { resource: Drawable, _, imageSource: ImageSource? -> onCompleteLambda?.invoke(resource, imageSource) },
                onClearMemoryLambda
        )
    }

    /**
     * Загрузка изображения в [CustomViewTarget]
     *
     * Используется для предотвращения утечек памяти при работе с [SimpleTarget]
     *
     * @param view элемент, в который будет происходить загрузка изображения
     * @param onErrorLambda лямбда, вызываемая при ошибке загрузки изображения.
     * @param onCompleteLambda лямбда, вызываемая при успешной загрузке изображения
     * @param onClearMemoryLambda лямбда, вызываемая, когда view может быть очищена. В ней следует
     * производить операции по дополнительному освобождению памяти.
     */
    fun <V : View> into(
            view: V,
            onErrorLambda: ((errorDrawable: Drawable?) -> Unit)? = null,
            onCompleteLambda: ((resource: Drawable, transition: Transition<in Drawable>?, imageSource: ImageSource?) -> Unit)? = null,
            onClearMemoryLambda: ((placeholder: Drawable?) -> Unit)? = null
    ) {
        buildRequest().into(
                if (view is ImageView) {
                    getDrawableImageViewTargetObject(view, onErrorLambda, onCompleteLambda, onClearMemoryLambda)
                } else {
                    getCustomViewTargetObject(view, onErrorLambda, onCompleteLambda, onClearMemoryLambda)
                }
        )
    }

    /**
     * Формирование запроса на загрузку изображения.
     */
    private fun buildRequest(): RequestBuilder<Drawable> = Glide.with(context)
            .load(imageResourceManager.toLoad())
            .addErrorIf(imageResourceManager.isErrorSet) { imageResourceManager.prepareErrorDrawable() }
            .addThumbnailIf(imageResourceManager.isPreviewSet) { imageResourceManager.preparePreviewDrawable() }
            .addTransitionIf(imageTransitionManager.isTransitionSet, imageTransitionManager.imageTransitionOptions)
            .apply(
                    RequestOptions()
                            .disableHardwareConfigIf(imageResourceManager.isHardwareConfigDisabled)
                            .dontAnimateIf(imageResourceManager.isAnimationDisabled)
                            .diskCacheStrategy(if (imageCacheManager.skipCache) {
                                DiskCacheStrategy.NONE
                            } else {
                                imageCacheManager.cacheStrategy.toGlideStrategy()
                            })
                            .skipMemoryCache(imageCacheManager.skipCache)
                            .downsample(if (imageTransformationsManager.isDownsampled) DownsampleStrategy.AT_MOST else DownsampleStrategy.NONE)
                            .sizeMultiplier(imageTransformationsManager.sizeMultiplier)
                            .applyTransformations(imageTransformationsManager.prepareTransformations())
                            .signature(ObjectKey(imageSignatureManager.signature
                                    ?: imageResourceManager.url))
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
            intoBackground(view)
        }
    }

    /**
     * Загрузка изображения в бекграунд к [View]
     */
    private fun intoBackground(view: View) {
        into(
                view,
                view::setBackground,
                { resource, _ -> view.background = resource },
                view::setBackground
        )
    }

    /**
     * Возвращает [Target], наследуемый от [CustomViewTarget]
     */
    private fun <V : View> getCustomViewTargetObject(
            view: V,
            onErrorLambda: ((errorDrawable: Drawable?) -> Unit)?,
            onCompleteLambda: ((resource: Drawable, transition: Transition<in Drawable>?, imageSource: ImageSource?) -> Unit)?,
            onClearMemoryLambda: ((placeholder: Drawable?) -> Unit)?
    ): CustomViewTarget<V, Drawable> {
        return object : CustomViewTarget<V, Drawable>(view) {

            override fun onLoadFailed(errorDrawable: Drawable?) {
                onErrorLambda?.invoke(errorDrawable)
            }

            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                onCompleteLambda?.invoke(resource, transition, imageCacheManager.imageSource)
            }

            override fun onResourceCleared(placeholder: Drawable?) {
                onClearMemoryLambda?.invoke(placeholder)
            }
        }
    }

    /**
     * Возвращает [Target], наследуемый от [DrawableImageViewTarget]
     */
    private fun getDrawableImageViewTargetObject(
            view: ImageView,
            onErrorLambda: ((errorDrawable: Drawable?) -> Unit)?,
            onCompleteLambda: ((resource: Drawable, transition: Transition<in Drawable>?, imageSource: ImageSource?) -> Unit)?,
            onClearMemoryLambda: ((placeholder: Drawable?) -> Unit)?
    ): DrawableImageViewTarget {
        return object : DrawableImageViewTarget(view) {

            override fun onLoadFailed(errorDrawable: Drawable?) {
                onErrorLambda?.invoke(errorDrawable)
            }

            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                onCompleteLambda?.invoke(resource, transition, imageCacheManager.imageSource)
            }

            override fun onLoadCleared(placeholder: Drawable?) {
                onClearMemoryLambda?.invoke(placeholder)
                super.onLoadCleared(placeholder)
            }
        }
    }

    //region Deprecated
    /**
     * Загрузка изображения в [SimpleTarget].
     *
     * @Deprecated строжайше запрещается использование этого метода из-за утечек памяти, связанных с [SimpleTarget]
     *
     * @param simpleTarget обработчик загрузки изображения.
     */
    @Deprecated("SimpleTarget из Glide помечен как @Deprecated, следует использовать версию into() с очисткой ресурсов")
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
     * @Deprecated строжайше запрещается использование этого метода из-за утечек памяти, связанных с [SimpleTarget]
     *
     * @param resourceReadyLambda колбек в случае успеха
     * @param loadFailedLambda колбек при ошибке
     */
    @Deprecated("SimpleTarget из Glide помечен как @Deprecated, следует использовать версию into() с очисткой ресурсов")
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
    //endregion
}