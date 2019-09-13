package ru.surfstudio.android.utilktx.data.wrapper.blockable

import ru.surfstudio.android.utilktx.data.wrapper.DataWrapperInterface
import ru.surfstudio.android.utilktx.data.wrapper.filterAndApply

/**
 * Extension-функции для коллекции, использующая [BlockableData]
 */

/**
 * Изменить блокировку value на противоположный,
 * используя предикат
 */
fun <T, E> Collection<E>.toggleBlocked(predicate: (T) -> Boolean)
        where E : DataWrapperInterface<T>, E : BlockableDataInterface {
    filterAndApply(this, { predicate(it) }, { it.toggleBlocked() })
}

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
 * Изменить блокировку value на противоположный
 */
fun <T, E> Collection<E>.toggleBlocked(value: T)
        where E : DataWrapperInterface<T>, E : BlockableDataInterface {
    toggleBlocked(predicate = { it == value })
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