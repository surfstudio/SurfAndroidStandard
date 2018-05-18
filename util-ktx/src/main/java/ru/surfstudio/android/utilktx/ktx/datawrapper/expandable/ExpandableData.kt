package ru.surfstudio.android.utilktx.ktx.datawrapper.expandable

import ru.surfstudio.android.utilktx.ktx.datawrapper.BaseDataWrapper

/**
 * Если объект может быть выделяемым
 */
interface ExpandableDataInterface {
    var isExpanded: Boolean
    fun expand()
    fun collapse()
}

/**
 * Поддерживает множество выделений
 */
class ExpandableData<T>(data: T)
    : BaseDataWrapper<T>(data), ExpandableDataInterface {

    override var isExpanded: Boolean = false

    override fun expand() {
        isExpanded = true
    }

    override fun collapse() {
        isExpanded = false
    }
}