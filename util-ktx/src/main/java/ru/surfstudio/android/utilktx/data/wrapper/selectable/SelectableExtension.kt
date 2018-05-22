package ru.surfstudio.android.utilktx.data.wrapper.selectable

import ru.surfstudio.android.utilktx.data.wrapper.DataWrapperInterface

/**
 * Extension-функции для коллекции, использующая [SelectableData]
 * у [SelectableData] может быть выделен только один элемент
 *
 * Если необходимо множественное выделение -> смотри [CheckableData]
 */

/**
 * Поставить выделение для <T>, используя предикат
 */
fun <T, E> Collection<E>.setSelected(predicate: (T) -> Boolean)
        where E : DataWrapperInterface<T>, E : SelectableDataInterface {
    setDeselected()
    findSingleAndApply(this, { predicate(it) }, { it.isSelected = true })
}

/**
 * Поставить выделение для <T>
 */
fun <T, E> Collection<E>.setSelected(value: T)
        where E : DataWrapperInterface<T>, E : SelectableDataInterface {
    setSelected(predicate = { it == value })
}

/**
 * @return возвращает выделенный объект <T>.
 */
fun <T, E> Collection<E>.getSelectedData(): T
        where E : DataWrapperInterface<T>, E : SelectableDataInterface {
    return getSelectedDataNullable()
            ?: throw IllegalStateException("ни одного выделенного элемента")
}

/**
 * @return возвращает выделенный объект <T>. Если ни один не выделен, то null
 */
fun <T, E> Collection<E>.getSelectedDataNullable(): T?
        where E : DataWrapperInterface<T>, E : SelectableDataInterface {
    val foundedItems = this.filter { it.isSelected }
    if (foundedItems.size > 1) throw IllegalStateException("было найдено больше одного элемента")

    return if (foundedItems.isEmpty()) {
        null
    } else {
        foundedItems.first().data
    }
}

/**
 * убирает выделение у всей коллекции
 */
fun <T, E> Collection<E>.setDeselected()
        where E : DataWrapperInterface<T>, E : SelectableDataInterface {
    this.forEach {
        it.apply {
            this.isSelected = false
        }
    }
}

/**
 * ставит выделение на первый элемент
 */
fun <T, E> Collection<E>.setSelectedFirst()
        where E : DataWrapperInterface<T>, E : SelectableDataInterface {
    setDeselected()
    this.first().apply {
        this.isSelected = true
    }
}

/**
 * @return есть ли выделенный объект?
 */
fun <T, E> Collection<E>.isAnySelected(): Boolean
        where E : DataWrapperInterface<T>, E : SelectableDataInterface =
        this.find { it.isSelected } != null

/**
 * Найти объект (только один) в коллекции по findPredicate
 * и изменить в соответствии с applyConsumer
 */
private fun <T, E : DataWrapperInterface<T>> findSingleAndApply(collection: Collection<E>,
                                                                findPredicate: (T) -> Boolean,
                                                                applyConsumer: (E) -> Unit) {
    val foundedItems = collection.filter { findPredicate(it.data) }
    if (foundedItems.size > 1) {
        throw IllegalStateException("было найдено больше одного элемента")
    } else {
        foundedItems.forEach(applyConsumer)
    }
}