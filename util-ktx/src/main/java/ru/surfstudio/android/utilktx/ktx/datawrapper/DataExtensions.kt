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
 * Найти объект(ы) в коллекции по filterPredicate
 * и изменить в соответствии с applyConsumer
 */
fun <T, E : BaseDataWrapper<T>> filterAndApply(collection: Collection<E>,
                                               filterPredicate: (T) -> Boolean,
                                               applyConsumer: (E) -> Unit) {
    collection
            .filter { filterPredicate(it.data) }
            .forEach { applyConsumer(it) }
}

/**
 * Найти объект (только один) в коллекции по findPredicate
 * и изменить в соответствии с applyConsumer
 */
fun <T, E : BaseDataWrapper<T>> findAndApply(collection: Collection<E>,
                                             findPredicate: (T) -> Boolean,
                                             applyConsumer: (E) -> Unit) {
    val foundedItem = collection.find { findPredicate(it.data) }
    if (foundedItem != null) {
        foundedItem.apply { applyConsumer(this) }
    } else {
        throw IllegalStateException("element not found in the findPredicate")
    }
}
