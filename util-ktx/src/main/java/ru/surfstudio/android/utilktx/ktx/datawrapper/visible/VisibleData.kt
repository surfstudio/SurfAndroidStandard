package ru.surfstudio.android.utilktx.ktx.datawrapper.visible

import ru.surfstudio.android.utilktx.ktx.datawrapper.DataWrapperInterface

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