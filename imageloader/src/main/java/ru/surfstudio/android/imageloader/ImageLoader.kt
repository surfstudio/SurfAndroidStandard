package ru.surfstudio.android.imageloader

import android.content.Context
import android.support.annotation.DrawableRes
import android.util.Patterns
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder

/**
 * Загрузчик изображений.
 *
 * Является надстройкой над библиотекой Glide.
 */
class ImageLoader(private val context: Context) : ImageLoaderInterface {

    private var targetView: View? = null    //целевая View, в которой отрисовывается изображение
    private var url: String = ""            //сетевая ссылка на изображение
    @DrawableRes
    private var drawableUri: Int = DEFAULT_DRAWABLE_URI //ссылка на drawable-ресурс

    companion object {
        fun with(context: Context) = ImageLoader(context)
    }

    /**
     * Загрузка изображения из сети
     *
     * @param url сетевая ссылка на изображение
     */
    @Throws(IllegalArgumentException::class)
    override fun url(url: String): ImageLoader {
        if (!Patterns.WEB_URL.matcher(url).matches()) {
            throw IllegalArgumentException("ImageLoader.url() / Некорректная ссылка на изображение: $url")
        }
        this.url = url
        return this
    }

    /**
     * Указание целевой [ImageView]
     *
     * @param imageView экземпляр [ImageView] для загрузки изображения
     */
    override fun into(imageView: ImageView) {
        this.targetView = imageView
        /*if (drawableUri == 0 && url == null) {
            imageView.setImageDrawable(getCompatDrawable(errorResId))
            return
        }*/

        /*if (imageView.getTag(R.id.image_loader_tag) != null && url == imageView.getTag(R.id.image_loader_tag)) {
            return
        }*/

        imageView.setTag(R.id.image_loader_tag, url)
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
                .load(if (isImageFromResources()) drawableUri else url)
        /*.error(errorBitmap)
        .thumbnail(placeholderBitmap)
        .apply(RequestOptions()
                .diskCacheStrategy(if (skipCache) DiskCacheStrategy.NONE else DiskCacheStrategy.ALL)
                .skipMemoryCache(skipCache)
                .transforms(*transformations))
        .listener(glideListener)*/
    }

    /**
     * Загружается ли изображение из res/drawable
     */
    private fun isImageFromResources() = drawableUri != DEFAULT_DRAWABLE_URI
}