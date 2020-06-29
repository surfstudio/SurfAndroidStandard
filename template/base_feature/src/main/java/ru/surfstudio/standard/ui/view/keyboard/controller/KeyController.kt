package ru.surfstudio.standard.ui.view.keyboard.controller

import android.view.ViewGroup
import ru.surfstudio.standard.ui.view.keyboard.keys.TextKey

/**
 * Контроллер для отображения кнопки с символом/текстом
 */
class KeyController(val onClick: (String) -> Unit) : BaseKeyController<TextKey, BaseKeyHolder<TextKey>>() {
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
        }
    }
}