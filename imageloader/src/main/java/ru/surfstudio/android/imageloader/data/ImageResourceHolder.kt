package ru.surfstudio.android.imageloader.data

import android.support.annotation.DrawableRes
import android.util.Patterns
import ru.surfstudio.android.imageloader.DEFAULT_DRAWABLE_URI

/**
 * Пакет со ссылками на все необходимые изображения и сервисными методами.
 */
data class ImageResourceHolder(
        var url: String = "",                           //сетевая ссылка на изображение
        @DrawableRes
        var drawableResId: Int = DEFAULT_DRAWABLE_URI,  //ссылка на drawable-ресурс
        @DrawableRes
        var errorResId: Int = DEFAULT_DRAWABLE_URI      //ссылка на drawable-ресурс при ошибке
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
     * Проверка валидность URL
     *
     * @param url проверяемая ссылка
     */
    fun isUrlValid(url: String) = !Patterns.WEB_URL.matcher(url).matches()

    /**
     * Проверка на наличие изображения для загрузки (из ресурсов или из сети).
     *
     * Если изображение не предоставлено - устанавливается [errorResId].
     */
    private fun isImagePresented(): Boolean {
        if (!isImageFromResourcesPresented() &&
                !isImageFromNetworkPresented()) {
            return false
        }
        return true
    }

    /**
     * Загружается ли изображение из сети
     */
    private fun isImageFromNetworkPresented() = url.isNotEmpty()

    /**
     * Загружается ли изображение из res/drawable
     */
    private fun isImageFromResourcesPresented() = drawableResId != DEFAULT_DRAWABLE_URI

    /**
     * Предоставлено ли изображение для ошибки
     */
    private fun isErrorPresented() = errorResId != DEFAULT_DRAWABLE_URI
}