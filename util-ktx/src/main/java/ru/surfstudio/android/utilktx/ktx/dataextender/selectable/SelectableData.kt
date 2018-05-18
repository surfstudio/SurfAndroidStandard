package ru.surfstudio.android.utilktx.ktx.dataextender.selectable

import ru.surfstudio.android.utilktx.ktx.dataextender.BaseDataExtender

/**
 * Если объект может быть выделяемым
 */
interface SelectableDataInterface {
    var isSelected: Boolean
    fun toggleSelected()
}

/**
 * Поддерживает одиночное выделение
 */
class SelectableData<T>(data: T)
    : BaseDataExtender<T>(data), SelectableDataInterface {

    override var isSelected: Boolean = false

    override fun toggleSelected() {
        isSelected = !isSelected
    }
}