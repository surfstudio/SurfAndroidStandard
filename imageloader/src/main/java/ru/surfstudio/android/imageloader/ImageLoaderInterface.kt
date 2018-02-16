package ru.surfstudio.android.imageloader

import android.graphics.Bitmap
import android.support.annotation.DrawableRes
import android.view.View
import android.widget.ImageView

/**
 * Универсальный интерфейс загрузчика изображений.
 */
interface ImageLoaderInterface {

    /**
     * Загрузка изображения из сети
     *
     * @param url сетевая ссылка на изображение
     */
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
     * Установка лямбды для отслеживания ошибки при загрузке изображения
     *
     * @param lambda лямбда, возвращающая ошибку [Throwable]
     */
    fun errorListener(lambda: ((throwable: Throwable) -> (Unit))): ImageLoader

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
     * Указание целевой [View]
     *
     * @param view экземпляр [View] для загрузки изображения
     */
    fun into(view: View)
}