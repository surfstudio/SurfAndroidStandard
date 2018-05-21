package ru.surfstudio.android.utilktx.ktx.datawrapper.visible

import ru.surfstudio.android.utilktx.ktx.datawrapper.filterAndApply

/**
 * Extension-функции для коллекции, использующая [VisibleData]
 */

/**
 * Показать элемент, используя предикат
 */
fun <T> Collection<VisibleData<T>>.show(predicate: (T) -> Boolean) {
    filterAndApply(this, { predicate(it) }, { it.show() })
}

/**
 * Скрыть элемент, используя предикат
 */
fun <T> Collection<VisibleData<T>>.hide(predicate: (T) -> Boolean) {
    filterAndApply(this, { predicate(it) }, { it.hide() })
}

/**
 * Показать элемент
 */
fun <T> Collection<VisibleData<T>>.show(value: T) {
    show(predicate = { it == value })
}

/**
 * Скрыть элемент
 */
fun <T> Collection<VisibleData<T>>.hide(value: T) {
    hide(predicate = { it == value })
}
