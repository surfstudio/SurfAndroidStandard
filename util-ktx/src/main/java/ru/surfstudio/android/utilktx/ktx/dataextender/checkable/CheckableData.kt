package com.company.uidata.dataextender.checkable

import com.company.uidata.dataextender.BaseDataExtender

/**
 * Если объект может быть выделяемым
 */
interface CheckableDataInterface {
    var isChecked: Boolean
    fun toggleChecked()
}

/**
 * Поддерживает множество выделений
 */
class CheckableData<T>(data: T)
    : BaseDataExtender<T>(data), CheckableDataInterface {

    override var isChecked: Boolean = false

    override fun toggleChecked() {
        isChecked = !isChecked
    }
}