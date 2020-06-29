package ru.surfstudio.standard.ui.view.keyboard.controller

import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.annotation.FontRes
import kotlinx.android.synthetic.main.item_key_view.view.*
import ru.surfstudio.standard.ui.view.keyboard.keys.TextKey

/**
 * Контроллер для отображения кнопки с символом/текстом
 */
class KeyController(
        val onClick: (String) -> Unit
) : BaseKeyController<TextKey, BaseKeyHolder<TextKey>>() {

    var textSize: Float? = null

    @ColorInt
    var textColor: Int? = null

    @FontRes
    var font: Int? = null

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

            textColor?.let { itemView.key_view.textColor = it }
            textSize?.let { itemView.key_view.textSize = it }
            font?.let { itemView.key_view.font = it }
        }
    }
}