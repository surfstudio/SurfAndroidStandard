package ru.surfstudio.android.datalistpagecount.ui

import ru.surfstudio.android.datalistpagecount.domain.datalist.DataList
import ru.surfstudio.android.easyadapter.pagination.PaginationState

/**
 * UI representation of paginated `DataList`.
 * */
class PaginationBundle<T>(
        val data: DataList<T>? = null,
        val state: PaginationState? = null
) {

    val hasData = !data.isNullOrEmpty()

    /**
     * Check that `data` and `state` is exist and if so - execute `callback`,
     * do nothing otherwise.
     * */
    fun safeGet(callback: (DataList<T>, PaginationState) -> Unit) {
        callback(data ?: return, state ?: return)
    }
}