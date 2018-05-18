package ru.surfstudio.android.utilktx.ktx.datawrapper.selectable

import ru.surfstudio.android.utilktx.ktx.datawrapper.BaseDataWrapper

/**
 * Интерфейс сущности, которая может быть выделяемым
 */
interface SelectableDataInterface {
    var isSelected: Boolean
    fun toggleSelected()
}

/**
 * Поддерживает одиночное выделение, используется в extension-функциях. [SelectableExtension]
 * Если необходимо множественное выделение -> смотри [CheckableData]
 */
class SelectableData<T>(data: T)
    : BaseDataWrapper<T>(data), SelectableDataInterface {

    override var isSelected: Boolean = false

    override fun toggleSelected() {
        isSelected = !isSelected
    }
}