package ru.surfstudio.android.utilktx.ktx.datawrapper.blockable

import ru.surfstudio.android.utilktx.ktx.datawrapper.filterAndApply

/**
 * Extension-функции для коллекции, использующая [BlockableData]
 */

/**
 * Блокировать value, используя предикат
 */
fun <T> Collection<BlockableData<T>>.setBlocked(predicate: (T) -> Boolean) {
    filterAndApply(this, { predicate(it) }, { it.block() })
}

/**
 * Разблокировать value, используя предикат
 */
fun <T> Collection<BlockableData<T>>.setUnblocked(predicate: (T) -> Boolean) {
    filterAndApply(this, { predicate(it) }, { it.unblock() })
}

/**
 * Блокировать value
 */
fun <T> Collection<BlockableData<T>>.setBlocked(value: T) {
    setBlocked(predicate = { it == value })
}

/**
 * Разблокировать value
 */
fun <T> Collection<BlockableData<T>>.setUnblocked(value: T) {
    setUnblocked(predicate = { it == value })
}
