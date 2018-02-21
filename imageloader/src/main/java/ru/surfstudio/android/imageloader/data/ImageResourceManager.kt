package ru.surfstudio.android.imageloader.data

import android.content.Context
import android.graphics.Bitmap
import android.support.annotation.DrawableRes
import android.util.Patterns
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.request.RequestOptions
import ru.surfstudio.android.imageloader.DEFAULT_DRAWABLE_URI

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
     * Следует ли сразу показать ошибку.
     *
     * Срабатывает при отсутствии ссылки на изображение и при наличии заглушки для ошибки.
     */
    fun isErrorState(): Boolean = !isImagePresented() && isErrorPresented()

    /**
     * Предоставлено ли изображение для ошибки
     */
    fun isErrorPresented() = errorResId != DEFAULT_DRAWABLE_URI

    /**
     * Подготовка заглушки для ошибки.
     *
     * К заглушке применяются все трансформации, применяемые и к исходному изображению.
     */
    fun prepareErrorBitmap() = prepareBitmap(errorResId)

    /**
     * Подготовка заглушки для плейсхолдера.
     *
     * К заглушке применяются все трансформации, применяемые и к исходному изображению.
     */
    fun preparePreviewBitmap() = prepareBitmap(previewResId)


    /**
     * Подготовка [Bitmap] с применением всех трансформаций, применяемых и к исходному изображению.
     *
     * @param imageResId ссылка на drawable ресурс
     */
    private fun prepareBitmap(@DrawableRes imageResId: Int): RequestBuilder<Bitmap> {
        return Glide.with(context)
                .asBitmap()
                .load(imageResId)
                .apply(RequestOptions()
                        .transforms(*imageTransformationsManager.prepareTransformations()))
    }

    /**
     * Проверка на наличие изображения для загрузки (из ресурсов или из сети).
     *
     * Если изображение не предоставлено - устанавливается [errorResId].
     */
    private fun isImagePresented() =
            isImageFromResourcesPresented() || isImageFromNetworkPresented()

    /**
     * Загружается ли изображение из сети
     */
    private fun isImageFromNetworkPresented() = url.isNotEmpty()

    /**
     * Загружается ли изображение из res/drawable
     */
    private fun isImageFromResourcesPresented() = drawableResId != DEFAULT_DRAWABLE_URI
}