package ru.surfstudio.android.utilktx.ktx.datawrapper

/**
 * Extension-функции для [DataWrapperInterface]
 */

fun <T> Collection<DataWrapperInterface<T>>.getData(): Collection<T> =
        this.map { it.data }

fun <T> Collection<DataWrapperInterface<T>>.getListData(): List<T> =
        this.map { it.data }

fun <T> Collection<DataWrapperInterface<T>>.getSetData(): Set<T> =
        this.map { it.data }.toSet()

/**
 * Найти объект(ы) в коллекции по filterPredicate
 * и изменить в соответствии с applyConsumer
 */
fun <T, E : DataWrapperInterface<T>> filterAndApply(collection: Collection<E>,
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
fun <T, E : DataWrapperInterface<T>> findFirstAndApply(collection: Collection<E>,
                                                       findPredicate: (T) -> Boolean,
                                                       applyConsumer: (E) -> Unit) {
    if (isFoundedMoreThanOne(collection, findPredicate)) {
        throw IllegalStateException("было найдено больше одного элемента")
    }
    filterAndApply(collection, findPredicate, applyConsumer)
}

/**
 * @return true - если по предикату найдено больше 1-го элемента
 */
fun <T, E : DataWrapperInterface<T>> isFoundedMoreThanOne(collection: Collection<E>,
                                                                  findPredicate: (T) -> Boolean): Boolean =
        collection.filter { findPredicate(it.data) }.size > 1