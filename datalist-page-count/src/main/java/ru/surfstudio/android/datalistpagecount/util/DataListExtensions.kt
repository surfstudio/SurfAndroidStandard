package ru.surfstudio.android.datalistpagecount.util

import ru.surfstudio.android.datalistpagecount.domain.datalist.DataList

/**
 * Создание пустого DataList
 *
 * @param totalItemsCount суммарное количество элементов в новом DataList
 * @param totalPagesCount суммарное количество страниц в новом DataList
 */
fun <T> emptyDataListOf(totalItemsCount: Int = 0, totalPagesCount: Int = 0) =
        DataList.emptyWithTotalCount<T>(totalItemsCount, totalPagesCount)

/**
 * Трансформация списка
 *
 * @param transform функция трансформации элемента
 *
 * @return трансформированный DataList
 */
fun <T, R> DataList<T>.map(transform: (T) -> R): DataList<R> {
    val result = this.asIterable().map(transform)
    return DataList(result, startPage, numPages, pageSize, this.totalItemsCount, this.totalPagesCount)
}