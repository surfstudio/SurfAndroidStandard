package ru.surfstudio.android.utilktx.ktx.datawrapper.blockable

import ru.surfstudio.android.utilktx.ktx.datawrapper.DataWrapperInterface
import ru.surfstudio.android.utilktx.ktx.datawrapper.filterAndApply

/**
 * Extension-функции для коллекции, использующая [BlockableData]
 */

/**
 * Блокировать value, используя предикат
 */
fun <T, E> Collection<E>.setBlocked(predicate: (T) -> Boolean)
        where E : DataWrapperInterface<T>, E : BlockableDataInterface {
    filterAndApply(this, { predicate(it) }, { it.block() })
}

/**
 * Разблокировать value, используя предикат
 */
fun <T, E> Collection<E>.setUnblocked(predicate: (T) -> Boolean)
        where E : DataWrapperInterface<T>, E : BlockableDataInterface {
    filterAndApply(this, { predicate(it) }, { it.unblock() })
}

/**
 * Блокировать value
 */
fun <T, E> Collection<E>.setBlocked(value: T)
        where E : DataWrapperInterface<T>, E : BlockableDataInterface {
    setBlocked(predicate = { it == value })
}

/**
 * Разблокировать value
 */
fun <T, E> Collection<E>.setUnblocked(value: T)
        where E : DataWrapperInterface<T>, E : BlockableDataInterface {
    setUnblocked(predicate = { it == value })
}