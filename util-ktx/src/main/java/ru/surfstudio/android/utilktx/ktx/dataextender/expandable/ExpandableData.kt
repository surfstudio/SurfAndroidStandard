package com.company.uidata.dataextender.expandable

import com.company.uidata.dataextender.BaseDataExtender

/**
 * Если объект может быть выделяемым
 */
interface ExpandableDataInterface {
    var isExpanded: Boolean
    fun show()
    fun hide()
}

/**
 * Поддерживает множество выделений
 */
class ExpandableData<T>(data: T)
    : BaseDataExtender<T>(data), ExpandableDataInterface {

    override var isExpanded: Boolean = false

    override fun show() {
        isExpanded = true
    }

    override fun hide() {
        isExpanded = false
    }
}