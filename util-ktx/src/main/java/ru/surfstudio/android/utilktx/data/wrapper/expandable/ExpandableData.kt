package ru.surfstudio.android.utilktx.data.wrapper.expandable

import ru.surfstudio.android.utilktx.data.wrapper.DataWrapperInterface

/**
 * Если объект может быть сворачиваемым
 */
interface ExpandableDataInterface {

    var isExpanded: Boolean

    fun expand() {
        isExpanded = true
    }

    fun collapse() {
        isExpanded = false
    }

    fun toggleExpandable() {
        isExpanded = !isExpanded
    }
}

data class ExpandableData<T>(override var data: T,
                             override var isExpanded: Boolean = false)
    : DataWrapperInterface<T>, ExpandableDataInterface