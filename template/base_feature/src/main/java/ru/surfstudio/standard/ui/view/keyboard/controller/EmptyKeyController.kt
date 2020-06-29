package ru.surfstudio.standard.ui.view.keyboard.controller

import android.view.ViewGroup
import ru.surfstudio.standard.ui.view.keyboard.keys.EmptyKey

/**
 * Контроллер для отображения пустого простарства на клавиатуре
 */
class EmptyKeyController : BaseKeyController<EmptyKey, BaseKeyHolder<EmptyKey>>() {

    override fun getItemId(key: EmptyKey): String {
        return key.toString()
    }

    override fun createViewHolder(parent: ViewGroup): BaseKeyHolder<EmptyKey> {
        return Holder(parent)
    }

    inner class Holder(parent: ViewGroup) : BaseKeyHolder<EmptyKey>(parent)
}
