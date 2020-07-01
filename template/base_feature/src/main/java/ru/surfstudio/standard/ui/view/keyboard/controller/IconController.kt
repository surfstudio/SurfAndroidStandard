package ru.surfstudio.standard.ui.view.keyboard.controller

import android.view.ViewGroup
import ru.surfstudio.standard.ui.view.keyboard.BaseIconKey

/**
 * Контроллер для отображения кнопки с иконкой
 */
class IconController(val onClick: () -> Unit) : BaseKeyController<BaseIconKey, BaseKeyHolder<BaseIconKey>>() {

    override fun getItemId(key: BaseIconKey): String {
        return key.hashCode().toString()
    }

    override fun createViewHolder(parent: ViewGroup): BaseKeyHolder<BaseIconKey> {
        return Holder(parent)
    }

    inner class Holder(parent: ViewGroup) : BaseKeyHolder<BaseIconKey>(parent) {

        init {
            itemView.setOnClickListener {
                onClick()
            }
        }
    }
}