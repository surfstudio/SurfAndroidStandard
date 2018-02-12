package ru.surfstudio.android.imageloader

import android.graphics.Bitmap
import android.support.annotation.DrawableRes
import android.widget.ImageView

interface ImageLoaderInterface {

    /**
     * Загрузка изображения из сети
     *
     * @param url сетевая ссылка на изображение
     */
    @Throws(IllegalArgumentException::class)
    fun url(url: String): ImageLoader

    /**
     * Загрузка изображения из ресурсов
     *
     * @param drawableResId ссылка на ресурс из папки res/drawable
     */
    fun url(@DrawableRes drawableResId: Int): ImageLoader

    /**
     * Указание графического ресурса, отображаемого в случае ошибки загрузки
     *
     * @param drawableResId ссылка на ресурс из папки res/drawable
     */
    fun error(@DrawableRes drawableResId: Int): ImageLoader

    /**
     * Установка лямбды для отслеживания загрузки изображения
     *
     * @param lambda лямбда, возвращающая загруженный [Bitmap]
     */
    fun listener(lambda: ((bitmap: Bitmap) -> (Unit))): ImageLoader

    /**
     * Указание политики кэширования.
     * Метод предоставляет возможность отключить кэширование загруженных изображений в памяти и на диске.
     *
     * @param skipCache true - игнорировать кэш в памяти и на диске, false - использовать кэш в памяти и на диске
     */
    fun skipCache(skipCache: Boolean = true): ImageLoader

    /**
     * Установка максимальной ширины изображения в px.
     *
     * Необходима для пережатия изображения без искажения пропорций.
     */
    fun maxWidth(maxWidth: Int): ImageLoader

    /**
     * Установка максимальной высоты изображения в px.
     *
     * Необходима для пережатия изображения без искажения пропорций.
     */
    fun maxHeight(maxHeight: Int): ImageLoader

    /**
     * Указание целевой [ImageView]
     *
     * @param imageView экземпляр [ImageView] для загрузки изображения
     */
    fun into(imageView: ImageView)
}