package ru.surfstudio.android.utilktx.ktx.datawrapper.visible

import ru.surfstudio.android.utilktx.ktx.datawrapper.DataWrapperInterface

/**
 * Интерфейс сущности, которая имеет возможность скрываться
 */
interface VisibleDataInterface {
    var isVisible: Boolean
    fun show()
    fun hide()
}

class VisibleData<T>(override var data: T)
    : DataWrapperInterface<T>, VisibleDataInterface {

    override var isVisible: Boolean = true

    override fun show() {
        isVisible = true
    }

    override fun hide() {
        isVisible = false
    }
}