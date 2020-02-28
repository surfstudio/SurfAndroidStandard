package ru.surfstudio.android.imageloader.data

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

/**
 * Пакет с информацией о переходах между изображениями
 */
class ImageTransitionManager {
    private val DEFAULT_TRANSITION_OPTS = DrawableTransitionOptions()

    val isTransitionSet: Boolean get() = imageTransitionOptions != DEFAULT_TRANSITION_OPTS

    var imageTransitionOptions = DEFAULT_TRANSITION_OPTS
}