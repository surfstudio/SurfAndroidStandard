package ru.surfstudio.android.imageloader.data

import ru.surfstudio.android.imageloader.NO_SIZE

/**
 * Пакет с размерами изображения
 */
data class ImageSizeHolder(
        var maxWidth: Int = NO_SIZE,    //максимальная ширина изображения в px
        var maxHeight: Int = NO_SIZE    //максимальная высота изображения в px
) {

    fun isMaxWidthSetUp() = maxWidth != NO_SIZE

    fun isMaxHeightSetUp() = maxHeight != NO_SIZE
}