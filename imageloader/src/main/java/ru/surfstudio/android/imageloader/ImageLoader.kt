package ru.surfstudio.android.imageloader

import android.content.Context
import android.support.annotation.DrawableRes
import android.util.Patterns
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import ru.surfstudio.android.logger.Logger

/**
 * Загрузчик изображений.
 *
 * Является надстройкой над библиотекой Glide.
 */
class ImageLoader(private val context: Context) : ImageLoaderInterface {

    private var targetView: View? = null    //целевая View, в которой отрисовывается изображение
    private var url: String = ""            //сетевая ссылка на изображение
    @DrawableRes
    private var drawableResId: Int = DEFAULT_DRAWABLE_URI   //ссылка на drawable-ресурс
    @DrawableRes
    private var errorResId: Int = DEFAULT_DRAWABLE_URI      //ссылка на drawable-ресурс при ошибке

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
            this.apply {
                if (isUrlValid(url)) {
                    Logger.e("ImageLoader.url() / Некорректная ссылка на изображение: $url")
                }
                this.url = url
            }

    /**
     * Загрузка изображения из ресурсов
     *
     * @param drawableResId ссылка на ресурс из папки res/drawable
     */
    override fun url(@DrawableRes drawableResId: Int) =
            this.apply { this.drawableResId = drawableResId }

    /**
     * Указание графического ресурса, отображаемого в случае ошибки загрузки
     *
     * @param drawableResId ссылка на ресурс из папки res/drawable
     */
    override fun error(@DrawableRes drawableResId: Int) =
            this.apply { this.errorResId = drawableResId }

    /**
     * Указание целевой [ImageView]
     *
     * @param imageView экземпляр [ImageView] для загрузки изображения
     */
    override fun into(imageView: ImageView) {
        this.targetView = imageView

        if (!isImagePresented(imageView)) return
        if (isTagUsed(imageView)) return

        setTag(imageView)
        buildRequest().into(imageView)
    }

    private fun buildRequest(): RequestBuilder<*> {
        /*val transforms = ArrayList<Transformation<*>>()
        transforms.add(SizeTransformation(context,
                maxWidth, maxHeight, filterBitmapOnScale))

        if (centerCrop) {
            transforms.add(CenterCrop())
        }

        if (circle) {
            transforms.add(CircleCropTransformation(context))
        }

        if (cornerRadius > 0 || cornerMargin > 0) {
            transforms.add(RoundedCornersTransformation(context,
                    cornerRadius, cornerMargin, cornerType))
        }

        if (blurTransform) {
            transforms.add(BlurTransformation(context, blurRadius, blurSampling))
        }

        if (overlayResId > 0) {
            transforms.add(OverlayTransformation(context, overlayResId))
        }

        // масковая трансформация должна накладываться последней
        // для обрезания конечного результата
        if (maskResId > 0) {
            transforms.add(MaskTransformation(context, maskResId))
        }

        val transformations = arrayOfNulls<Transformation<*>>(transforms.size)
        transforms.toTypedArray<Transformation>()

        val errorBitmap = Glide.with(context)
                .asBitmap()
                .load(errorResId)
                .apply(RequestOptions()
                        .transforms(*transformations))
        val placeholderBitmap = Glide.with(context)
                .asBitmap()
                .load(previewResId)
                .apply(RequestOptions()
                        .transforms(*transformations))
        //.bitmapTransform(new Transform());*/
        return Glide.with(context)
                .asBitmap()
                .load(if (isImageFromResourcesPresented()) drawableResId else url)
        /*.error(errorBitmap)
        .thumbnail(placeholderBitmap)
        .apply(RequestOptions()
                .diskCacheStrategy(if (skipCache) DiskCacheStrategy.NONE else DiskCacheStrategy.ALL)
                .skipMemoryCache(skipCache)
                .transforms(*transformations))
        .listener(glideListener)*/
    }

    /**
     * Проверка на наличие изображения для загрузки (из ресурсов или из сети).
     *
     * Если изображение не предоставлено - устанавливается [errorResId].
     */
    private fun isImagePresented(imageView: ImageView): Boolean {
        if (!isImageFromResourcesPresented() && !isImageFromNetworkPresented()) {
            if (isErrorPresented()) {
                setErrorImage(imageView)
            }
            return false
        }
        return true
    }

    /**
     * Установка заглушки ошибки для [ImageView]
     *
     * @param imageView экземпляр [ImageView], куда устанавливается заглушка
     */
    private fun setErrorImage(imageView: ImageView) {
        imageView.setImageResource(errorResId)
    }

    /**
     * Загружается ли изображение из res/drawable
     */
    private fun isImageFromResourcesPresented() = drawableResId != DEFAULT_DRAWABLE_URI

    /**
     * Загружается ли изображение из сети
     */
    private fun isImageFromNetworkPresented() = url.isNotEmpty()

    /**
     * Предоставлено ли изображение для ошибки
     */
    private fun isErrorPresented() = errorResId != DEFAULT_DRAWABLE_URI

    /**
     * Проверка валидность URL
     *
     * @param url проверяемая ссылка
     */
    private fun isUrlValid(url: String) = !Patterns.WEB_URL.matcher(url).matches()

    /**
     * Установка тэга на [ImageView]
     */
    private fun setTag(imageView: ImageView) {
        imageView.setTag(R.id.image_loader_tag, url)
    }

    /**
     * Извлечения тэга
     */
    private fun getTag(imageView: ImageView) = imageView.getTag(R.id.image_loader_tag)

    /**
     * Проверка тэга на то, был ли он ранее уже использован
     */
    private fun isTagUsed(imageView: ImageView) =
            getTag(imageView) != null && url == getTag(imageView)
}