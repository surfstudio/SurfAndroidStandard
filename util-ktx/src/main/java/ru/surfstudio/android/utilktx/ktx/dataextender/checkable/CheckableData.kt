package ru.surfstudio.android.utilktx.ktx.dataextender.checkable

import ru.surfstudio.android.utilktx.ktx.dataextender.BaseDataExtender

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