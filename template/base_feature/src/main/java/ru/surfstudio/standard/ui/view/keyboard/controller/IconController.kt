package ru.surfstudio.standard.ui.view.keyboard.controller

import android.view.ViewGroup
import ru.surfstudio.standard.ui.view.keyboard.IconKey

/**
 * Контроллер для отображения кнопки с иконкой
 * todo удалить, если не требуется на проекте
 */
class IconController : BaseKeyController<IconKey, BaseKeyHolder<IconKey>>() {

    override fun getItemId(key: IconKey) = key.hashCode().toString()

    override fun createViewHolder(parent: ViewGroup) = Holder(parent)

    inner class Holder(parent: ViewGroup) : BaseKeyHolder<IconKey>(parent) {

        override fun bind(key: IconKey) {
            super.bind(key)

            itemView.setOnClickListener {
                key.onClickListener()
            }
        }
    }
}