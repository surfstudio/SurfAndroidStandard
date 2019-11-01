package ru.surfstudio.android.imageloader.util

import android.graphics.Bitmap
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.TransitionOptions
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import ru.surfstudio.android.imageloader.data.CacheStrategy
import ru.surfstudio.android.imageloader.data.ImageSource

/**
 * Функция-расширение для добавления трансформаций
 * Если трансформаций нет, возвращается первоначальный объект
 *
 * @param transformations список трансформаций
 *
 * @return [RequestOptions] со списком трансформаций
 */
fun RequestOptions.applyTransformations(transformations: List<Transformation<Bitmap>>): RequestOptions {
    return if (transformations.isNotEmpty())
        transform(MultiTransformation(transformations))
    else
        this
}

/**
 * Функция-расширение для отключения конфигурации [Bitmap.Config.HARDWARE].
 *
 * @param condition условие, при выполнении которого будет выключена
 *                  конфигурация [Bitmap.Config.HARDWARE].
 */
fun RequestOptions.disableHardwareConfigIf(condition: Boolean): RequestOptions {
    return if (condition) this.disallowHardwareConfig() else this
}

/**
 * Функция-расширение для отключения анимаций.
 *
 * @param condition условие, при выполнении которого будут выключены анимации.
 */
fun RequestOptions.dontAnimateIf(condition: Boolean): RequestOptions {
    return if (condition) this.dontAnimate() else this
}

/**
 * Функция-расширение для добавления плейсхолдера в случае ошибки загрузки изображения
 * Срабатывает только при выполнении условия condition
 *
 * @param condition условие, при выполнении которого после ошибки будет показано превью
 * @param errorRequest запрос, с помощью которого будет загружен плейсхолдер
 */
fun <T> RequestBuilder<T>.addErrorIf(
        condition: Boolean,
        errorRequest: () -> RequestBuilder<T>
): RequestBuilder<T> {
    return if (condition) this.error(errorRequest()) else this
}

/**
 * Функция-расширение для добавления плейсхолдера перед началом загрузки изображения
 * Срабатывает только при выполнении условия condition
 *
 * @param condition условие, при выполнении которого перед загрузкой будет показано превью
 * @param thumbnailRequest запрос, с помощью которого будет загружен плейсхолдер
 */
fun <T> RequestBuilder<T>.addThumbnailIf(
        condition: Boolean,
        thumbnailRequest: () -> RequestBuilder<T>
): RequestBuilder<T> {
    return if (condition) this.thumbnail(thumbnailRequest()) else this
}

/**
 * Функция-расширение для добавления анимации перехода между плейсхолдером и актуальной версией изображения
 * Срабатывает только при выполнении условия condition
 *
 * @param condition условие, при выполнении которого будет осуществлена анимация
 * @param transitionOptions опции, которые описывают анимацию
 */
fun <T, Child : TransitionOptions<Child, T>> RequestBuilder<T>.addTransitionIf(
        condition: Boolean,
        transitionOptions: TransitionOptions<Child, T>
): RequestBuilder<T> {
    return if (condition) this.transition(transitionOptions) else this
}

/**
 * Трансформирование [CacheStrategy] в [DiskCacheStrategy] из [Glide]
 */
internal fun CacheStrategy.toGlideStrategy() = when (ordinal) {
    CacheStrategy.CACHE_ALL.ordinal -> DiskCacheStrategy.ALL
    CacheStrategy.CACHE_ORIGINAL.ordinal -> DiskCacheStrategy.DATA
    CacheStrategy.CACHE_TRANSFORMED.ordinal -> DiskCacheStrategy.RESOURCE
    CacheStrategy.CACHE_NOTHING.ordinal -> DiskCacheStrategy.NONE
    else -> DiskCacheStrategy.AUTOMATIC
}

/**
 * Трансформация [DataSource] из [Glide] в обертку [ImageSource]
 */
internal fun DataSource.toImageSource(): ImageSource = when (ordinal) {
    DataSource.LOCAL.ordinal -> ImageSource.LOCAL
    DataSource.DATA_DISK_CACHE.ordinal, DataSource.RESOURCE_DISK_CACHE.ordinal -> ImageSource.DISK_CACHE
    DataSource.MEMORY_CACHE.ordinal -> ImageSource.MEMORY_CACHE
    else -> ImageSource.REMOTE
}