package ru.surfstudio.android.imageloader

import android.graphics.Bitmap
import android.support.annotation.DrawableRes
import android.support.annotation.WorkerThread
import android.view.View
import ru.surfstudio.android.imageloader.transformations.RoundedCornersTransformation.CornerType

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
     * Указание графического ресурса, отображаемого в качестве плейсхолдера
     *
     * @param drawableResId ссылка на ресурс из папки res/drawable
     */
    fun preview(@DrawableRes drawableResId: Int): ImageLoader

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
     * Масштабирование изображения по размеру виджета с обрезкой излишков.
     *
     * @param isCrop флаг активации трансформации
     */
    fun centerCrop(isCrop: Boolean = true): ImageLoader

    /**
     * Преобразование прямоугольного изображения в круглое.
     *
     * @param isCircle флаг активации трансформации
     */
    fun circle(isCircle: Boolean = true): ImageLoader

    /**
     * Скругление углов у прямоугольного изображения.
     *
     * @param isRoundedCorners флаг активации трансформации
     * @param radiusPx радиус скругления в px
     * @param marginPx величина отступа в px
     * @param cornerType конфигурация скругляемых углов
     */
    fun roundedCorners(isRoundedCorners: Boolean = true,
                       radiusPx: Int = 0,
                       marginPx: Int = 0,
                       cornerType: CornerType = CornerType.ALL): ImageLoader

    /**
     * Эффект размытия изображения "Blur".
     *
     * @param isBlur флаг активации трансформации
     * @param blurRadiusPx радиус размытия
     * @param blurDownSampling уровень принудительного понижения качества разрешения изображения
     */
    fun blur(isBlur: Boolean = true,
             blurRadiusPx: Int = 25,
             blurDownSampling: Int = 1): ImageLoader

    /**
     * Наложение маски на изображение с поддержкой 9-patch маски.
     *
     * @param isOverlay флаг активации трансформации
     * @param maskResId ссылка на ресурс изображения маски из папки res/drawable
     */
    fun mask(isOverlay: Boolean = true,
             @DrawableRes maskResId: Int): ImageLoader

    /**
     * Указание целевой [View].
     *
     * @param view экземпляр [View] для загрузки изображения.
     */
    fun into(view: View)

    /**
     * Получение исходника изображения в формате [Bitmap].
     *
     * Запрос происходит в UI-потоке.
     */
    @WorkerThread
    fun get(): Bitmap?
}