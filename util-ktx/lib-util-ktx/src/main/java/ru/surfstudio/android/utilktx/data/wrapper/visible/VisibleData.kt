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

    fun toggleVisibility() {
        isVisible = !isVisible
    }
}

data class VisibleData<T>(override var data: T,
                          override var isVisible: Boolean = true)
    : DataWrapperInterface<T>, VisibleDataInterface