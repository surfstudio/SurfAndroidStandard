package ru.surfstudio.android.imageloader.data

/**
 * Класс, отвечающий за кэширование изображения
 */
data class ImageCacheManager(
        var skipCache: Boolean = false //использовать ли закэшированные данные
)