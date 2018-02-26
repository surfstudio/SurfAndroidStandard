package ru.surfstudio.android.imageloader.data

import android.view.View
import ru.surfstudio.android.imageloader.R

/**
 * Класс, отвечающий за актуализацию тэгов при загрузке изображения
 */
data class ImageTagManager(
        private var imageTargetManager: ImageTargetManager,
        private var imageResourceManager: ImageResourceManager
) {

    /**
     * Проверка тэга на то, был ли он ранее уже использован
     */
    fun isTagUsed() =
            getTag() != null && imageResourceManager.url == getTag()

    /**
     * Установка тэга на целевую [View]
     */
    fun setTag(url: String?) {
        imageTargetManager.targetView?.setTag(R.id.image_loader_tag, url)
    }

    /**
     * Извлечение тэга
     */
    private fun getTag() = imageTargetManager.targetView?.getTag(R.id.image_loader_tag)
}