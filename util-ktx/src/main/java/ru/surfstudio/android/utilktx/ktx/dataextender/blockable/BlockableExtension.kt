package ru.surfstudio.android.utilktx.ktx.dataextender.blockable

/**
 * Extension-функции для коллекции, использующая [BlockableData]
 */

/**
 * Блокировать value
 */
fun <T> Collection<BlockableData<T>>.setBlocked(value: T) {
    this
            .find { it.data == value }
            .apply { this!!.block() }
}

/**
 * Разблокировать value
 */
fun <T> Collection<BlockableData<T>>.setUnblocked(value: T) {
    this
            .find { it.data == value }
            .apply { this!!.unblock() }
}

/**
 * Блокировать value, используя предикат
 */
fun <T> Collection<BlockableData<T>>.setBlocked(predicate: (T) -> Boolean) {
    this
            .find { predicate(it.data) }
            .apply { this!!.block() }
}

/**
 * Разблокировать value, используя предикат
 */
fun <T> Collection<BlockableData<T>>.setUnblocked(predicate: (T) -> Boolean) {
    this
            .find { predicate(it.data) }
            .apply { this!!.unblock() }
}