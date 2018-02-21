package ru.surfstudio.android.imageloader.data

import android.content.Context
import android.view.View
import android.widget.ImageView

/**
 * Пакет с описанием целей загрузки изображений
 */
data class ImageTargetManager(
        private val context: Context,
        private val imageResourceManager: ImageResourceManager = ImageResourceManager(context),
        var targetView: View? = null    //целевая View
) {
    /**
     * Установка заглушки ошибки для [View]
     *
     * @param view экземпляр [View], куда устанавливается заглушка
     */
    fun setErrorImage() {
        if (targetView is ImageView) {
            (targetView as ImageView).setImageResource(imageResourceManager.errorResId)
        } else {
            targetView?.setBackgroundResource(imageResourceManager.errorResId)
        }
    }
}