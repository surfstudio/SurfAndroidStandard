package ru.surfstudio.standard.ui.view.keyboard.controller

import android.view.ViewGroup
import ru.surfstudio.standard.ui.view.keyboard.EmptyKey

/**
 * Контроллер для отображения пустого пространства на клавиатуре
 * todo удалить, если не требуется на проекте
 */
class EmptyKeyController : BaseKeyController<EmptyKey, BaseKeyHolder<EmptyKey>>() {

    override fun getItemId(key: EmptyKey) = key.toString()

    override fun createViewHolder(parent: ViewGroup) = Holder(parent)

    class Holder(parent: ViewGroup) : BaseKeyHolder<EmptyKey>(parent)
}
