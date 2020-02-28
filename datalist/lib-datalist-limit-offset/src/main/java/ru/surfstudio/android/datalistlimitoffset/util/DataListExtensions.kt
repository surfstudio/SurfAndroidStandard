package ru.surfstudio.android.datalistlimitoffset.util

import ru.surfstudio.android.datalistlimitoffset.domain.datalist.DataList

/**
 * Создание пустого DataList
 *
 * @param totalCount суммарное количество элементов в новом DataList
 */
fun <T> emptyDataListOf(totalCount: Int = 0) = DataList.emptyWithTotal<T>(totalCount)

/**
 * Фильтрация списка по указанному условию
 *
 * @param predicate функция условия фильтрации
 *
 * @return отфильтрованный DataList
 */
fun <T> DataList<T>.filter(predicate: (T) -> Boolean): DataList<T> {
    val result = this.asIterable().filter(predicate)
    return DataList(result, limit, offset, totalCount)
}

/**
 * Трансформация списка
 *
 * @param transform функция трансформации элемента
 *
 * @return трансформированный DataList
 */
fun <T, R> DataList<T>.map(transform: (T) -> R): DataList<R> {
    val result = this.asIterable().map(transform)
    return DataList(result, limit, offset, totalCount)
}