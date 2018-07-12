package ru.surfstudio.android.utilktx.data.wrapper.deletable

import ru.surfstudio.android.utilktx.data.wrapper.DataWrapperInterface
import ru.surfstudio.android.utilktx.data.wrapper.filterAndApply

/**
 * Extension-функции для коллекции, использующая [DeletableData]
 */

/**
 * Удалить элемент, с возможностью возвращение,
 * используя предикат
 */
fun <T, E> Collection<E>.setMarkAsDeleted(predicate: (T) -> Boolean)
        where E : DataWrapperInterface<T>, E : DeletableDataInterface {
    filterAndApply(this, { predicate(it) }, { it.markAsDeleted() })
}

/**
 * Возврат после удаления,
 * используя предикат
 */
fun <T, E> Collection<E>.setUndo(predicate: (T) -> Boolean)
        where E : DataWrapperInterface<T>, E : DeletableDataInterface {
    filterAndApply(this, { predicate(it) }, { it.undo() })
}

/**
 * Удалить элемент, с возможностью возвращение
 */
fun <T, E> Collection<E>.setMarkAsDeleted(value: T)
        where E : DataWrapperInterface<T>, E : DeletableDataInterface {
    setMarkAsDeleted(predicate = { it == value })
}

/**
 * Возврат после удаления
 */
fun <T, E> Collection<DeletableData<T>>.setUndo(value: T)
        where E : DataWrapperInterface<T>, E : DeletableDataInterface {
    setUndo(predicate = { it == value })
}