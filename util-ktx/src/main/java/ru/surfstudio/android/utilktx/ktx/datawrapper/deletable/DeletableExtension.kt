package ru.surfstudio.android.utilktx.ktx.datawrapper.deletable

import ru.surfstudio.android.utilktx.ktx.datawrapper.filterAndApply

/**
 * Extension-функции для коллекции, использующая [DeletableData]
 */

/**
 * Удалить элемент, с возможностью возвращение,
 * используя предикат
 */
fun <T> Collection<DeletableData<T>>.setMarkAsDeleted(predicate: (T) -> Boolean) {
    filterAndApply(this, { predicate(it) }, { it.markAsDeleted() })
}

/**
 * Возврат после удаления,
 * используя предикат
 */
fun <T> Collection<DeletableData<T>>.setUndo(predicate: (T) -> Boolean) {
    filterAndApply(this, { predicate(it) }, { it.undo() })
}

/**
 * Удалить элемент, с возможностью возвращение
 */
fun <T> Collection<DeletableData<T>>.setMarkAsDeleted(value: T) {
    setMarkAsDeleted(predicate = { it == value })
}

/**
 * Возврат после удаления
 */
fun <T> Collection<DeletableData<T>>.setUndo(value: T) {
    setUndo(predicate = { it == value })
}