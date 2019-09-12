package ru.surfstudio.android.datalistpagecount.util

import ru.surfstudio.android.datalistpagecount.domain.datalist.DataList

/**
 * Creates empty DataList
 *
 * @param totalItemsCount maximum number of elements in new DataList
 * @param totalPagesCount maximum number of pages in new DataList
 */
fun <T> emptyDataListOf(totalItemsCount: Int = 0, totalPagesCount: Int = 0) =
        DataList.emptyWithTotalCount<T>(totalItemsCount, totalPagesCount)

/**
 * Transforms elements in DataList
 *
 * @param transform mapper function
 *
 * @return transformed DataList
 */
fun <T, R> DataList<T>.map(transform: (T) -> R): DataList<R> {
    val result = this.asIterable().map(transform)
    return DataList(result, startPage, numPages, pageSize, this.totalItemsCount, this.totalPagesCount)
}