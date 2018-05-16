package com.company.uidata.dataextender.selectable

import com.company.uidata.dataextender.BaseDataExtender

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