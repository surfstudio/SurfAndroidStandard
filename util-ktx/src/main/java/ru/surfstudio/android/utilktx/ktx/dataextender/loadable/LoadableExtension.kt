package ru.surfstudio.android.utilktx.ktx.dataextender.loadable

/**
 * Extension-функции для коллекции, использующая [LoadableData]
 */

fun <T> Collection<LoadableData<T>>.setLoadable(value: T, loadStateData: LoadStateData) {
    this
            .find { it.data == value }
            .apply { this!!.loadStateData = loadStateData }
}

fun <T> Collection<LoadableData<T>>.setLoadable(predicate: (T) -> Boolean, loadStateData: LoadStateData) {
    this
            .find { predicate(it.data) }
            .apply { this!!.loadStateData = loadStateData }
}