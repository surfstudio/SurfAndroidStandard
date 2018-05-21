package ru.surfstudio.android.utilktx.ktx.datawrapper.loadable

import ru.surfstudio.android.utilktx.ktx.datawrapper.filterAndApply

/**
 * Extension-функции для коллекции, использующая [LoadableData]
 */

/**
 * Поставить элементу состояние загрузки
 */
fun <T> Collection<LoadableData<T>>.setLoading(predicate: (T) -> Boolean) {
    filterAndApply(this, { predicate(it) }, { it.setLoading() })
}

/**
 * Сделать элемент в обычном состоянии
 */
fun <T> Collection<LoadableData<T>>.setLoadingNormalState(predicate: (T) -> Boolean) {
    filterAndApply(this, { predicate(it) }, { it.setNormal() })
}

/**
 * Поставить элемент в состояние ошибки
 */
fun <T> Collection<LoadableData<T>>.setLoadingErrorState(predicate: (T) -> Boolean) {
    filterAndApply(this, { predicate(it) }, { it.setErrorLoading() })
}

/**
 * Поставить элементу состояние загрузки
 */
fun <T> Collection<LoadableData<T>>.setLoading(value: T) {
    setLoading(predicate = { it == value })
}

/**
 * Сделать элемент в обычном состоянии
 */
fun <T> Collection<LoadableData<T>>.setLoadingNormalState(value: T) {
    setLoadingNormalState(predicate = { it == value })
}

/**
 * Поставить элемент в состояние ошибки
 */
fun <T> Collection<LoadableData<T>>.setLoadingNormal(value: T) {
    setLoadingErrorState(predicate = { it == value })
}