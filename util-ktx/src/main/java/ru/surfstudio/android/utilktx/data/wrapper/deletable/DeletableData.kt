package ru.surfstudio.android.utilktx.data.wrapper.deletable

import ru.surfstudio.android.utilktx.data.wrapper.DataWrapperInterface

/**
 * Интерфейс сущности, которая должна уметь удаляться с возможностью возвращения
 */
interface DeletableDataInterface {

    var isDeleted: Boolean

    fun markAsDeleted() {
        isDeleted = true
    }

    fun undo() {
        isDeleted = false
    }
}

class DeletableData<T>(override var data: T)
    : DataWrapperInterface<T>, DeletableDataInterface {

    override var isDeleted: Boolean = false
}