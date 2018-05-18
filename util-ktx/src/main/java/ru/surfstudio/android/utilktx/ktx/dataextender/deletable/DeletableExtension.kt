package ru.surfstudio.android.utilktx.ktx.dataextender.deletable

/**
 * Extension-функции для коллекции, использующая [DeletableData]
 */

/**
 * Удалить элемент, с возможностью возвращение
 */
fun <T> Collection<DeletableData<T>>.setBlocked(value: T) {
    this
            .find { it.data == value }
            .apply { this!!.delete() }
}

/**
 * Возврат после удаления
 */
fun <T> Collection<DeletableData<T>>.setUnblocked(value: T) {
    this
            .find { it.data == value }
            .apply { this!!.undo() }
}

/**
 * Удалить элемент, с возможностью возвращение,
 * используя предикат
 */
fun <T> Collection<DeletableData<T>>.setBlocked(predicate: (T) -> Boolean) {
    this
            .find { predicate(it.data) }
            .apply { this!!.delete() }
}

/**
 * Возврат после удаления,
 * используя предикат
 */
fun <T> Collection<DeletableData<T>>.setUnblocked(predicate: (T) -> Boolean) {
    this
            .find { predicate(it.data) }
            .apply { this!!.undo() }
}