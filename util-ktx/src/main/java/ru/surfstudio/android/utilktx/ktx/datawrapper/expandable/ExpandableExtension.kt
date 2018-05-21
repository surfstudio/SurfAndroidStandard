package ru.surfstudio.android.utilktx.ktx.datawrapper.expandable

import ru.surfstudio.android.utilktx.ktx.datawrapper.DataWrapperInterface
import ru.surfstudio.android.utilktx.ktx.datawrapper.filterAndApply

/**
 * Extension-функции для коллекции, использующая [ExpandableData]
 */

/**
 * Развернуть элемент, используя предикат
 */
fun <T, E> Collection<E>.expand(predicate: (T) -> Boolean)
        where E : DataWrapperInterface<T>, E : ExpandableDataInterface {
    filterAndApply(this, { predicate(it) }, { it.expand() })
}

/**
 * Развернуть элемент, используя предикат и свернуть другие развёрнутые
 */
fun <T, E> Collection<E>.expandAndCollapseAnother(predicate: (T) -> Boolean)
        where E : DataWrapperInterface<T>, E : ExpandableDataInterface {
    collapseAll()
    expand(predicate)
}

/**
 * Развернуть элемент
 */
fun <T, E> Collection<E>.expand(value: T)
        where E : DataWrapperInterface<T>, E : ExpandableDataInterface {
    expand(predicate = { it == value })
}

/**
 * Развернуть элемент и свернуть другие развёрнутые
 */
fun <T, E> Collection<E>.expandAndCollapseAnother(value: T)
        where E : DataWrapperInterface<T>, E : ExpandableDataInterface {
    expandAndCollapseAnother(predicate = { it == value })
}

/**
 * Свернуть элемент, используя предикат
 */
fun <T, E> Collection<E>.collapse(predicate: (T) -> Boolean)
        where E : DataWrapperInterface<T>, E : ExpandableDataInterface {
    filterAndApply(this, { predicate(it) }, { it.collapse() })
}

/**
 * Свернуть элемент
 */
fun <T, E> Collection<E>.collapse(value: T)
        where E : DataWrapperInterface<T>, E : ExpandableDataInterface {
    collapse(predicate = { it == value })
}

/**
 * Показать всё
 */
fun <T, E> Collection<E>.expandAll()
        where E : DataWrapperInterface<T>, E : ExpandableDataInterface {
    this.forEach {
        it.apply { expand() }
    }
}

/**
 * Свернуть всё
 */
fun <T, E> Collection<E>.collapseAll()
        where E : DataWrapperInterface<T>, E : ExpandableDataInterface {
    this.forEach {
        it.apply { expand() }
    }
}