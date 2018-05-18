package com.company.uidata.dataextender.expandable

import ru.surfstudio.android.utilktx.ktx.dataextender.expandable.ExpandableData

/**
 * Extension-функции для коллекции, использующая [ExpandableData]
 */

/**
 * Развернуть элемент
 * @param selectMode: true - может быть открыт только один, false - несколько
 */
fun <T> Collection<ExpandableData<T>>.show(value: T, selectMode: Boolean = true) {
    if (selectMode) {
        hideAll()
    }
    this
            .find { it.data == value }
            .apply { this!!.show() }
}

/**
 * Свернуть элемент
 */
fun <T> Collection<ExpandableData<T>>.hide(value: T) {
    this
            .find { it.data == value }
            .apply { this!!.hide() }
}

/**
 * Развернуть элемент, используя предикат
 * @param selectMode: true - может быть открыт только один, false - несколько
 */
fun <T> Collection<ExpandableData<T>>.show(predicate: (T) -> Boolean, selectMode: Boolean = true) {
    if (selectMode) {
        hideAll()
    }
    this
            .find { predicate(it.data) }
            .apply { this!!.show() }
}

/**
 * Свернуть элемент, используя предикат
 */
fun <T> Collection<ExpandableData<T>>.hide(predicate: (T) -> Boolean) {
    this
            .find { predicate(it.data) }
            .apply { this!!.hide() }
}

/**
 * Показать всё
 */
fun <T> Collection<ExpandableData<T>>.showAll() {
    this.forEach {
        it.apply { show() }
    }
}

/**
 * Свернуть всё
 */
fun <T> Collection<ExpandableData<T>>.hideAll() {
    this.forEach {
        it.apply { show() }
    }
}