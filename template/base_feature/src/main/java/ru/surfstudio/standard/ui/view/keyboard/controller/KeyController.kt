package ru.surfstudio.standard.ui.view.keyboard.controller

import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.annotation.FontRes
import ru.surfstudio.standard.ui.view.keyboard.TextKey

/**
 * Контроллер для отображения кнопки с символом/текстом
 * todo удалить, если не требуется на проекте
 */
class KeyController(
        var titleTextSize: Float,
        var subtitleTextSize: Float,
        @ColorInt var titleTextColor: Int,
        @ColorInt var subtitleTextColor: Int,
        @FontRes var titleFont: Int,
        @FontRes var subtitleFont: Int,
        var subtitleMargin: Float,
        var isSubtitleVisible: Boolean = true
) : BaseKeyController<TextKey, BaseKeyHolder<TextKey>>() {

    override fun getItemId(key: TextKey) = "${key.hashCode()}${isSubtitleVisible.hashCode()}"

    override fun createViewHolder(parent: ViewGroup) = Holder(parent)

    inner class Holder(parent: ViewGroup) : BaseKeyHolder<TextKey>(parent) {

        override fun bind(key: TextKey) {
            super.bind(key)
            itemView.setOnClickListener {
                key.onClickListener(key.code)
            }

            keyView.titleColor = titleTextColor
            keyView.titleTextSize = titleTextSize
            keyView.titleFont = titleFont

            keyView.subtitleTextSize = subtitleTextSize
            keyView.subtitleColor = subtitleTextColor
            keyView.subtitleFont = subtitleFont
            keyView.subtitleMargin = subtitleMargin

            keyView.isSubtitleVisible = isSubtitleVisible
        }
    }
}