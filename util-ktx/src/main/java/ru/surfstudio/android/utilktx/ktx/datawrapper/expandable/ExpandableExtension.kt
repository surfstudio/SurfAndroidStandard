package ru.surfstudio.android.utilktx.ktx.datawrapper.expandable

import ru.surfstudio.android.utilktx.ktx.datawrapper.filterAndApply

/**
 * Extension-функции для коллекции, использующая [ExpandableData]
 */

/**
 * Развернуть элемент, используя предикат
 */
fun <T> Collection<ExpandableData<T>>.expand(predicate: (T) -> Boolean) {
    filterAndApply(this, { predicate(it) }, { it.expand() })
}

/**
 * Развернуть элемент, используя предикат и свернуть другой развёрнутый
 */
fun <T> Collection<ExpandableData<T>>.expandAndCollapseAnother(predicate: (T) -> Boolean) {
    collapseAll()
    expand(predicate)
}

/**
 * Развернуть элемент
 */
fun <T> Collection<ExpandableData<T>>.expand(value: T) {
    expand(predicate = { it == value })
}

/**
 * Развернуть элемент и свернуть другой развёрнутый
 */
fun <T> Collection<ExpandableData<T>>.expandAndCollapseAnother(value: T) {
    collapseAll()
    expand(value)
}

/**
 * Свернуть элемент, используя предикат
 */
fun <T> Collection<ExpandableData<T>>.collapse(predicate: (T) -> Boolean) {
    filterAndApply(this, { predicate(it) }, { it.collapse() })
}

/**
 * Свернуть элемент
 */
fun <T> Collection<ExpandableData<T>>.collapse(value: T) {
    collapse(predicate = { it == value })
}

/**
 * Показать всё
 */
fun <T> Collection<ExpandableData<T>>.expandAll() {
    this.forEach {
        it.apply { expand() }
    }
}

/**
 * Свернуть всё
 */
fun <T> Collection<ExpandableData<T>>.collapseAll() {
    this.forEach {
        it.apply { expand() }
    }
}