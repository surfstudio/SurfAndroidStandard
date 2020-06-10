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

    fun toggleDeleted() {
        isDeleted = !isDeleted
    }
}

data class DeletableData<T>(override var data: T,
                            override var isDeleted: Boolean = false)
    : DataWrapperInterface<T>, DeletableDataInterface