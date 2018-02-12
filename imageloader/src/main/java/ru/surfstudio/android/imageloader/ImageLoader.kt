package ru.surfstudio.android.imageloader

import android.content.Context
import android.graphics.Bitmap
import android.support.annotation.DrawableRes
import android.util.Patterns
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import ru.surfstudio.android.imageloader.data.ImageResourceHolder
import ru.surfstudio.android.imageloader.data.ImageSizeHolder
import ru.surfstudio.android.logger.Logger

/**
 * Загрузчик изображений.
 *
 * Является надстройкой над библиотекой Glide.
 */
class ImageLoader(private val context: Context) : ImageLoaderInterface {

    private var targetView: View? = null    //целевая View, в которой отрисовывается изображение

    private var imageResourceHolder: ImageResourceHolder = ImageResourceHolder()
    private var imageSizeHolder: ImageSizeHolder = ImageSizeHolder()

    private var transformations = emptyArray<Transformation<Bitmap>>()   //список всех применяемых трансформаций

    private var skipCache: Boolean = false  //использовать ли закэшированные данные

    private var onImageLoadedLambda: ((bitmap: Bitmap) -> (Unit))? = null

    private val glideDownloadListener = object : RequestListener<Bitmap> {
        override fun onLoadFailed(e: GlideException?,
                                  model: Any?,
                                  target: Target<Bitmap>?,
                                  isFirstResource: Boolean) = false.apply { setTag(targetView, null) }

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
                if (imageResourceHolder.isUrlValid(url)) {
                    Logger.e("ImageLoader.url() / Некорректная ссылка на изображение: $url")
                }
                this.imageResourceHolder.url = url
            }

    /**
     * Загрузка изображения из ресурсов
     *
     * @param drawableResId ссылка на ресурс из папки res/drawable
     */
    override fun url(@DrawableRes drawableResId: Int) =
            apply { this.imageResourceHolder.drawableResId = drawableResId }

    /**
     * Указание графического ресурса, отображаемого в случае ошибки загрузки
     *
     * @param drawableResId ссылка на ресурс из папки res/drawable
     */
    override fun error(@DrawableRes drawableResId: Int) =
            apply { this.imageResourceHolder.errorResId = drawableResId }

    /**
     * Установка лямбды для отслеживания загрузки изображения
     *
     * @param lambda лямбда, возвращающая загруженный [Bitmap]
     */
    override fun listener(lambda: ((bitmap: Bitmap) -> (Unit))) =
            apply { this.onImageLoadedLambda = lambda }

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
            apply { this.imageSizeHolder.maxWidth = maxWidth }

    /**
     * Установка максимальной высоты изображения в px.
     *
     * Необходима для пережатия изображения без искажения пропорций.
     */
    override fun maxHeight(maxHeight: Int) =
            apply { this.imageSizeHolder.maxHeight = maxHeight }

    /**
     * Указание целевой [ImageView]
     *
     * @param imageView экземпляр [ImageView] для загрузки изображения
     */
    override fun into(imageView: ImageView) {
        this.targetView = imageView

        if (imageResourceHolder.isErrorState()) {
            setErrorImage(imageView)
        }

        if (isTagUsed(imageView)) return

        setTag(imageView, imageResourceHolder.url)
        buildRequest().into(imageView)
    }

    private fun buildRequest(): RequestBuilder<*> {
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
                .load(imageResourceHolder.toLoad())
                .error(prepareErrorBitmap())
                .apply(
                        RequestOptions()
                                .diskCacheStrategy(if (skipCache) DiskCacheStrategy.NONE else DiskCacheStrategy.RESOURCE)
                                .skipMemoryCache(skipCache)
                                .transforms(*transformations)
                )
                .listener(glideDownloadListener)
        /*
        .thumbnail(placeholderBitmap)
        .apply(RequestOptions()
                .transformations(*transformations))*/
    }

    /**
     * Подготовка всех требуемых трансформаций.
     */
    /*private fun prepareTransformations() =*/

    /**
     * Подготовка заглушки для ошибки.
     *
     * К заглушке применяются все трансформации, применяемые и к исходному изображению.
     */
    private fun prepareErrorBitmap() =
            Glide.with(context)
                    .asBitmap()
                    .load(imageResourceHolder.errorResId)
                    .apply(RequestOptions()
                            .transforms(*transformations))

    /**
     * Установка заглушки ошибки для [ImageView]
     *
     * @param imageView экземпляр [ImageView], куда устанавливается заглушка
     */
    private fun setErrorImage(imageView: ImageView) {
        imageView.setImageResource(imageResourceHolder.errorResId)
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
    private fun getTag(imageView: ImageView) = imageView.getTag(R.id.image_loader_tag)

    /**
     * Проверка тэга на то, был ли он ранее уже использован
     */
    private fun isTagUsed(imageView: ImageView) =
            getTag(imageView) != null && imageResourceHolder.url == getTag(imageView)
}