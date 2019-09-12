package ru.surfstudio.android.datalistlimitoffset.util

import ru.surfstudio.android.datalistlimitoffset.domain.datalist.DataList

/**
 * Creates empty DataList
 *
 * @param totalCount maximum number of elements in DataList
 */
fun <T> emptyDataListOf(totalCount: Int = 0) = DataList.emptyWithTotal<T>(totalCount)

/**
 * Filters elements in DataList by predicate
 *
 * @param predicate predicate with filter condition
 *
 * @return filtered DataList
 */
fun <T> DataList<T>.filter(predicate: (T) -> Boolean): DataList<T> {
    val result = this.asIterable().filter(predicate)
    return DataList(result, limit, offset, totalCount)
}

/**
 * Transforms elements in DataList
 *
 * @param transform mapper function
 *
 * @return transformed DataList
 */
fun <T, R> DataList<T>.map(transform: (T) -> R): DataList<R> {
    val result = this.asIterable().map(transform)
    return DataList(result, limit, offset, totalCount)
}