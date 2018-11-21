package ru.surfstudio.android.datalistlimitoffset.util

import ru.surfstudio.android.datalistlimitoffset.domain.datalist.DataList

/**
 * Создание пустого DataList
 */
fun <T> emptyDataListOf() = DataList.empty<T>()

/**
 * Фильтрация списка по указанному условию
 *
 * @param predicate функция условия фильтрации
 *
 * @return отфильтрованный DataList
 */
fun <T> DataList<T>.filter(predicate: (T) -> Boolean): DataList<T> {
    val result = (this as Iterable<T>).filter(predicate)
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
    val result = (this as Iterable<T>).map(transform)
    return DataList(result, limit, offset, totalCount)
}