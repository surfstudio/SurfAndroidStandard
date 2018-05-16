package com.company.uidata.dataextender.deletable

import com.company.uidata.dataextender.BaseDataExtender

/**
 * Если должен уметь удаляться с возможностью возвращения
 */
interface DeletableDataInterface {
    var isDeleted: Boolean
    fun delete()
    fun undo()
}

class DeletableData<T>(data: T)
    : BaseDataExtender<T>(data), DeletableDataInterface {

    override var isDeleted: Boolean = false

    override fun delete() {
        isDeleted = true
    }

    override fun undo() {
        isDeleted = false
    }
}