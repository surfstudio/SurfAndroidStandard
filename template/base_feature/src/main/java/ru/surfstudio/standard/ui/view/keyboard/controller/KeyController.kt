package ru.surfstudio.standard.ui.view.keyboard.controller

import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.annotation.FontRes
import kotlinx.android.synthetic.main.item_key_view.view.*
import ru.surfstudio.standard.ui.view.keyboard.TextKey

/**
 * Контроллер для отображения кнопки с символом/текстом
 * todo удалить, если не требуется на проекте
 */
class KeyController(
        val onClick: (String) -> Unit
) : BaseKeyController<TextKey, BaseKeyHolder<TextKey>>() {

    var titleTextSize: Float? = null
    var subtitleTextSize: Float? = null

    @ColorInt
    var titleTextColor: Int? = null

    @ColorInt
    var subtitleTextColor: Int? = null

    @FontRes
    var titleFont: Int? = null

    @FontRes
    var subtitleFont: Int? = null

    var subtitleMargin: Float? = null

    var isSubtitleVisible = false

    override fun getItemId(key: TextKey): String {
        return key.hashCode().toString()
    }

    override fun createViewHolder(parent: ViewGroup): BaseKeyHolder<TextKey> {
        return Holder(parent)
    }

    inner class Holder(parent: ViewGroup) : BaseKeyHolder<TextKey>(parent) {

        override fun bind(key: TextKey) {
            super.bind(key)
            itemView.setOnClickListener {
                onClick(key.code)
            }

            titleTextColor?.let { itemView.key_view.titleColor = it }
            titleTextSize?.let { itemView.key_view.titleTextSize = it }
            titleFont?.let { itemView.key_view.titleFont = it }

            subtitleTextSize?.let { itemView.key_view.subtitleTextSize = it }
            subtitleTextColor?.let { itemView.key_view.subtitleColor = it }
            subtitleFont?.let { itemView.key_view.subtitleFont = it }

            subtitleMargin?.let { itemView.key_view.subtitleMargin = it }

            itemView.key_view.isSubtitleVisible = isSubtitleVisible
        }
    }
}