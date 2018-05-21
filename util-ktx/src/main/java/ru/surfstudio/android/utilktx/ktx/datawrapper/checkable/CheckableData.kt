package ru.surfstudio.android.utilktx.ktx.datawrapper.checkable

import ru.surfstudio.android.utilktx.ktx.datawrapper.DataWrapperInterface

/**
 * Интерфейс сущности, если объект может быть выделяемым
 */
interface CheckableDataInterface {
    var isChecked: Boolean
    fun toggleChecked()
}

/**
 * Поддерживает множество выделений, через расширение коллекций
 * Если необходимо одиночное выделение -> смотри [SelectableData]
 */
class CheckableData<T>(override var data: T)
    : DataWrapperInterface<T>, CheckableDataInterface {

    override var isChecked: Boolean = false

    override fun toggleChecked() {
        isChecked = !isChecked
    }
}