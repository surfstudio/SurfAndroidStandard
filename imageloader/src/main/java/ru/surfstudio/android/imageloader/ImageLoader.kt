package ru.surfstudio.android.imageloader

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.target.ViewTarget
import com.bumptech.glide.request.transition.Transition
import ru.surfstudio.android.imageloader.data.ImageResourceManager
import ru.surfstudio.android.imageloader.data.ImageTargetManager
import ru.surfstudio.android.imageloader.data.ImageTransformationsManager
import ru.surfstudio.android.logger.Logger

/**
 * Загрузчик изображений.
 *
 * Является надстройкой над библиотекой Glide.
 */
class ImageLoader(private val context: Context) : ImageLoaderInterface {

    private var imageTargetManager: ImageTargetManager = ImageTargetManager()
    private var imageResourceManager: ImageResourceManager = ImageResourceManager()
    private var imageTransformationsManager: ImageTransformationsManager = ImageTransformationsManager()

    private var skipCache: Boolean = false  //использовать ли закэшированные данные

    private var onImageLoadedLambda: ((bitmap: Bitmap) -> (Unit))? = null
    private var onImageLoadErrorLambda: ((throwable: Throwable) -> (Unit))? = null

    private val glideDownloadListener = object : RequestListener<Bitmap> {
        override fun onLoadFailed(e: GlideException?,
                                  model: Any?,
                                  target: Target<Bitmap>?,
                                  isFirstResource: Boolean) = false.apply {
            setTag(imageTargetManager.targetView, null)
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
                if (imageResourceManager.isUrlValid(url)) {
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
            apply { this.skipCache = skipCache }

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
     * Указание целевой [View]
     *
     * @param view экземпляр [View] для загрузки изображения
     */
    override fun into(view: View) {
        this.imageTargetManager.targetView = view

        if (imageResourceManager.isErrorState()) {
            setErrorImage(view)
        }

        if (isTagUsed(view)) return

        setTag(view, imageResourceManager.url)

        performLoad(view)
    }

    private fun buildRequest(): RequestBuilder<Bitmap> {
        /*
        transformations.add(SizeTransformation(context,
                maxWidth, maxHeight, filterBitmapOnScale))

        if (centerCrop) {
            transformations.add(CenterCrop())
        }

        if (circle) {
            transformations.add(CircleCropTransformation(context))
        }

        if (cornerRadius > 0 || cornerMargin > 0) {
            transformations.add(RoundedCornersTransformation(context,
                    cornerRadius, cornerMargin, cornerType))
        }

        if (blurTransform) {
            transformations.add(BlurTransformation(context, blurRadius, blurSampling))
        }

        if (overlayResId > 0) {
            transformations.add(OverlayTransformation(context, overlayResId))
        }

        // масковая трансформация должна накладываться последней
        // для обрезания конечного результата
        if (maskResId > 0) {
            transformations.add(MaskTransformation(context, maskResId))
        }

        val transformations = arrayOfNulls<Transformation<*>>(transformations.size)
        transformations.toTypedArray<Transformation>()

        val placeholderBitmap = Glide.with(context)
                .asBitmap()
                .toLoad(previewResId)
                .apply(RequestOptions()
                        .transformations(*transformations))
        //.bitmapTransform(new Transform());*/

        //val transformations = arrayOfNulls<Transformation<*>>(transformations.size)

        //val transformations = ArrayList<Transformation<*>>()

        return Glide.with(context)
                .asBitmap()
                .load(imageResourceManager.toLoad())
                .error(prepareErrorBitmap())
                .apply(
                        RequestOptions()
                                .diskCacheStrategy(if (skipCache) DiskCacheStrategy.NONE else DiskCacheStrategy.ALL)
                                .skipMemoryCache(skipCache)
                                .transforms(*imageTransformationsManager.prepareTransformations())
                )
                .listener(glideDownloadListener)
        /*
        .thumbnail(placeholderBitmap)*/
    }

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

    /**
     * Подготовка заглушки для ошибки.
     *
     * К заглушке применяются все трансформации, применяемые и к исходному изображению.
     */
    private fun prepareErrorBitmap() =
            Glide.with(context)
                    .asBitmap()
                    .load(imageResourceManager.errorResId)
                    .apply(RequestOptions()
                            .transforms(*imageTransformationsManager.prepareTransformations()))

    /**
     * Установка заглушки ошибки для [View]
     *
     * @param view экземпляр [View], куда устанавливается заглушка
     */
    private fun setErrorImage(view: View) {
        if (view is ImageView) {
            view.setImageResource(imageResourceManager.errorResId)
        } else {
            //todo view.background = getCompatDrawable(errorResId)
        }
    }

    /**
     * Установка тэга на целевую [View]
     */
    private fun setTag(targetView: View?, url: String?) {
        targetView?.setTag(R.id.image_loader_tag, url)
    }

    /**
     * Извлечения тэга
     */
    private fun getTag(view: View) = view.getTag(R.id.image_loader_tag)

    /**
     * Проверка тэга на то, был ли он ранее уже использован
     */
    private fun isTagUsed(view: View) =
            getTag(view) != null && imageResourceManager.url == getTag(view)
}
