package ru.surfstudio.android.imageloader.data

import android.view.View

/**
 * Пакет с описанием целей загрузки изображений
 */
data class ImageTargetManager(
        var targetView: View? = null    //целевая View
)