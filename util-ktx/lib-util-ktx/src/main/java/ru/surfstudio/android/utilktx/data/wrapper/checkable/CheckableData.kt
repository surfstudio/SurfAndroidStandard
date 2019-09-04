package ru.surfstudio.android.utilktx.data.wrapper.checkable

import ru.surfstudio.android.utilktx.data.wrapper.DataWrapperInterface

/**
 * Интерфейс сущности, если объект может быть выделяемым
 */
interface CheckableDataInterface {

    var isChecked: Boolean

    fun toggleChecked() {
        isChecked = !isChecked
    }
}

/**
 * Поддерживает множество выделений, через расширение коллекций
 * Если необходимо одиночное выделение -> смотри [SelectableData]
 */
data class CheckableData<T>(override var data: T,
                            override var isChecked: Boolean = false)
    : DataWrapperInterface<T>, CheckableDataInterface