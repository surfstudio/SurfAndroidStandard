package ru.surfstudio.android.utilktx.data.wrapper.selectable

import ru.surfstudio.android.utilktx.data.wrapper.DataWrapperInterface
import ru.surfstudio.android.utilktx.data.wrapper.checkable.CheckableData
import java.io.Serializable

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
data class SelectableData<T>(override var data: T,
                             override var isSelected: Boolean = false)
    : DataWrapperInterface<T>, SelectableDataInterface, Serializable