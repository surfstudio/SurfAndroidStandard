package ru.surfstudio.android.utilktx.data.wrapper.loadable

import ru.surfstudio.android.utilktx.data.wrapper.DataWrapperInterface
import ru.surfstudio.android.utilktx.data.wrapper.filterAndApply

/**
 * Extension-функции для коллекции, использующая [LoadableData]
 */

/**
 * Поставить элементу состояние загрузки
 */
fun <T, E> Collection<E>.setLoading(predicate: (T) -> Boolean)
        where E : DataWrapperInterface<T>, E : LoadableDataInterface {
    filterAndApply(this, { predicate(it) }, { it.setLoading() })
}

/**
 * Сделать элемент в обычном состоянии
 */
fun <T, E> Collection<E>.setLoadingNormalStatus(predicate: (T) -> Boolean)
        where E : DataWrapperInterface<T>, E : LoadableDataInterface {
    filterAndApply(this, { predicate(it) }, { it.setNormal() })
}

/**
 * Поставить элемент в состояние ошибки
 */
fun <T, E> Collection<E>.setLoadingErrorStatus(predicate: (T) -> Boolean)
        where E : DataWrapperInterface<T>, E : LoadableDataInterface {
    filterAndApply(this, { predicate(it) }, { it.setErrorLoading() })
}

/**
 * Поставить элементу состояние загрузки
 */
fun <T, E> Collection<E>.setLoading(value: T)
        where E : DataWrapperInterface<T>, E : LoadableDataInterface {
    setLoading(predicate = { it == value })
}

/**
 * Сделать элемент в обычном состоянии
 */
fun <T, E> Collection<E>.setLoadingNormalStatus(value: T)
        where E : DataWrapperInterface<T>, E : LoadableDataInterface {
    setLoadingNormalStatus(predicate = { it == value })
}

/**
 * Поставить элемент в состояние ошибки
 */
fun <T, E> Collection<E>.setLoadingNormal(value: T)
        where E : DataWrapperInterface<T>, E : LoadableDataInterface {
    setLoadingErrorStatus(predicate = { it == value })
}