package ru.surfstudio.android.utilktx.data.wrapper.selectable

import ru.surfstudio.android.utilktx.data.wrapper.DataWrapperInterface

/**
 * Интерфейс сущности, которая может быть выделяемым
 */
interface SelectableDataInterface {

    var isSelected: Boolean

    fun toggleSelected() {
        isSelected = !isSelected
    }
}

/**
 * Поддерживает одиночное выделение, используется в extension-функциях. [SelectableExtension]
 * Если необходимо множественное выделение -> смотри [CheckableData]
 */
class SelectableData<T>(override var data: T)
    : DataWrapperInterface<T>, SelectableDataInterface {

    override var isSelected: Boolean = false
}