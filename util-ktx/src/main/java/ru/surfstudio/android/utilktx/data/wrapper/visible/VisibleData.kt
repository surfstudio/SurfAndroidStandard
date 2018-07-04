package ru.surfstudio.android.utilktx.data.wrapper.visible

import ru.surfstudio.android.utilktx.data.wrapper.DataWrapperInterface

/**
 * Интерфейс сущности, которая имеет возможность скрываться
 */
interface VisibleDataInterface {

    var isVisible: Boolean

    fun show() {
        isVisible = true
    }

    fun hide() {
        isVisible = false
    }
}

class VisibleData<T>(override var data: T)
    : DataWrapperInterface<T>, VisibleDataInterface {

    override var isVisible: Boolean = true
}