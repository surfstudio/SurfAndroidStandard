package ru.surfstudio.android.utilktx.ktx.datawrapper.visible

import ru.surfstudio.android.utilktx.ktx.datawrapper.BaseDataWrapper

/**
 * Интерфейс сущности, которая имеет возможность скрываться
 */
interface VisibleDataInterface {
    var isVisible: Boolean
    fun show()
    fun hide()
}

class VisibleData<T>(data: T)
    : BaseDataWrapper<T>(data), VisibleDataInterface {

    override var isVisible: Boolean = true

    override fun show() {
        isVisible = true
    }

    override fun hide() {
        isVisible = false
    }
}