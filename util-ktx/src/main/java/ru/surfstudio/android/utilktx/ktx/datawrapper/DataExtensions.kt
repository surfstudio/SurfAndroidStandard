package ru.surfstudio.android.utilktx.ktx.datawrapper

/**
 * Extension-функции для [BaseDataWrapper]
 */

fun <T> Collection<BaseDataWrapper<T>>.getData(): Collection<T> =
        this.map { it.data }

fun <T> Collection<BaseDataWrapper<T>>.getListData(): List<T> =
        this.map { it.data }

fun <T> Collection<BaseDataWrapper<T>>.getSetData(): Set<T> =
        this.map { it.data }.toSet()

/**
 * Найти объект(ы) в коллекции по findPredicate
 * и изменить в соответствии с applyConsumer
 */
fun <T, E : BaseDataWrapper<T>> findAndApply(collection: Collection<E>,
                                              findPredicate: (T) -> Boolean,
                                              applyConsumer: (E) -> Unit) {
    collection
            .filter { findPredicate(it.data) }
            .map { applyConsumer(it) }
}
