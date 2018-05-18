package ru.surfstudio.android.utilktx.ktx.datawrapper.checkable

import ru.surfstudio.android.utilktx.ktx.datawrapper.BaseDataWrapper

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
class CheckableData<T>(data: T)
    : BaseDataWrapper<T>(data), CheckableDataInterface {

    override var isChecked: Boolean = false

    override fun toggleChecked() {
        isChecked = !isChecked
    }
}