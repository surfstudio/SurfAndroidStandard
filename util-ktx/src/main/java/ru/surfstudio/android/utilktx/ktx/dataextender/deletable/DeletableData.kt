package ru.surfstudio.android.utilktx.ktx.dataextender.deletable

import ru.surfstudio.android.utilktx.ktx.dataextender.BaseDataExtender

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