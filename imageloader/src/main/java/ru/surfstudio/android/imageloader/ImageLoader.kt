package ru.surfstudio.android.imageloader

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.support.annotation.WorkerThread
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
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
import ru.surfstudio.android.imageloader.util.getBitmapFromRes
import ru.surfstudio.android.imageloader.util.toBitmap
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.android.utilktx.util.ValidationUtil
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

    private var onImageLoadedLambda: ((bitmap: Bitmap) -> (Unit))? = null
    private var onImageLoadErrorLambda: ((throwable: Throwable) -> (Unit))? = null

    private val glideDownloadListener = object : RequestListener<Bitmap> {
        override fun onLoadFailed(e: GlideException?,
                                  model: Any?,
                                  target: Target<Bitmap>?,
                                  isFirstResource: Boolean) = false.apply {
            imageTagManager.setTag(null)
            e?.let { onImageLoadErrorLambda?.invoke(it) }
        }

        override fun onResourceReady(resource: Bitmap,
                                     model: Any?,
                                     target: Target<Bitmap>?,
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
                if (ValidationUtil.isUrlValid(url)) {
                    Logger.e("ImageLoader.url() / Некорректная ссылка на изображение: $url")
                }
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
     * @param lambda лямбда, возвращающая загруженный [Bitmap]
     */
    override fun listener(lambda: ((bitmap: Bitmap) -> (Unit))) =
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

    /**
     * Указание целевой [View]
     *
     * @param view экземпляр [View] для загрузки изображения
     */
    override fun into(view: View) {
        this.imageTargetManager.targetView = view

        if (imageResourceManager.isErrorState()) {
            imageTargetManager.setErrorImage()
        }

        if (imageTagManager.isTagUsed()) return

        imageTagManager.setTag(imageResourceManager.url)

        performLoad(view)
    }

    /**
     * Загрузка изображения в [SimpleTarget].
     *
     * @param simpleTarget обработчик загрузки изображения.
     */
    fun into(simpleTarget: SimpleTarget<Bitmap>) {
        imageResourceManager.preparePreviewBitmap().into(object : SimpleTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                simpleTarget.onResourceReady(resource, transition)
            }

            override fun onLoadFailed(errorDrawable: Drawable?) {
                super.onLoadFailed(errorDrawable)
                getBitmapFromRes(context, imageResourceManager.errorResId)?.let {
                    simpleTarget.onResourceReady(it, null)
                }
            }
        })
    }

    /**
     * Получение исходника изображения в формате [Bitmap].
     *
     * Запрос происходит в UI-потоке.
     */
    @WorkerThread
    override fun get(): Bitmap? {
        var result: Bitmap?
        try {
            result = buildRequest()
                    .submit(NO_SIZE, NO_SIZE)
                    .get()
        } catch (e: Exception) {
            when (e) {
                is InterruptedException, is ExecutionException -> {
                    if (!imageResourceManager.isErrorPresented()) {
                        Logger.e("ImageLoader.get() / Ошибка загрузки изображения")
                        throw IllegalStateException("Ошибка загрузки изображения")
                    }
                    onImageLoadErrorLambda?.invoke(e)
                    result = getBitmapFromRes(context, imageResourceManager.errorResId)
                }
                else -> throw e
            }
        }
        return result
    }

    /**
     * Формирование запроса на загрузку изображения.
     */
    private fun buildRequest(): RequestBuilder<Bitmap> = Glide.with(context)
            .asBitmap()
            .load(imageResourceManager.toLoad())
            .error(imageResourceManager.prepareErrorBitmap())
            .thumbnail(imageResourceManager.preparePreviewBitmap())
            .apply(
                    RequestOptions()
                            .diskCacheStrategy(if (imageCacheManager.skipCache) DiskCacheStrategy.NONE else DiskCacheStrategy.ALL)
                            .skipMemoryCache(imageCacheManager.skipCache)
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
            buildRequest().into(object : ViewTarget<View, Bitmap>(view) {

                override fun onLoadStarted(placeholder: Drawable?) {
                    super.onLoadStarted(placeholder)
                    view.background = placeholder
                }

                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    view.background = BitmapDrawable(context.resources, resource)
                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    super.onLoadFailed(errorDrawable)
                    view.background = errorDrawable
                }
            })
        }
    }

}