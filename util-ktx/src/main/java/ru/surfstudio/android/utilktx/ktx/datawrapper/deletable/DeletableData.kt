package ru.surfstudio.android.utilktx.ktx.datawrapper.deletable

import ru.surfstudio.android.utilktx.ktx.datawrapper.DataWrapperInterface

/**
 * Интерфейс сущности, которая должна уметь удаляться с возможностью возвращения
 */
interface DeletableDataInterface {
    var isDeleted: Boolean
    fun markAsDeleted()
    fun undo()
}

class DeletableData<T>(override var data: T)
    : DataWrapperInterface<T>, DeletableDataInterface {

    override var isDeleted: Boolean = false

    override fun markAsDeleted() {
        isDeleted = true
    }

    override fun undo() {
        isDeleted = false
    }
}