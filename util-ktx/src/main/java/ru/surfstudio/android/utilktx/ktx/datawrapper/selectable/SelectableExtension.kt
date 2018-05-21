package ru.surfstudio.android.utilktx.ktx.datawrapper.selectable

import ru.surfstudio.android.utilktx.ktx.datawrapper.DataWrapperInterface
import ru.surfstudio.android.utilktx.ktx.datawrapper.findFirstAndApply

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
    setUnselected()
    findFirstAndApply(this, { predicate(it) }, { it.isSelected = true })
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
    if (!isAnySelected()) {
        throw IllegalStateException("ни одного выделенного элемента")
    }
    return this.filter { it.isSelected }.map { it.data }[0]
}

/**
 * @return возвращает выделенный объект <T>. Если ни один не выделе, то null
 */
fun <T, E> Collection<E>.getSelectedDataNullable(): T?
        where E : DataWrapperInterface<T>, E : SelectableDataInterface =
        this.find { it.isSelected }?.data

/**
 * убирает выделение у всей коллекции
 */
fun <T, E> Collection<E>.setUnselected()
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
    setUnselected()
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