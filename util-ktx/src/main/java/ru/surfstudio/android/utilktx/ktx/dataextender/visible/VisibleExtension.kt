package ru.surfstudio.android.utilktx.ktx.dataextender.visible

/**
 * Extension-функции для коллекции, использующая [VisibleData]
 */

/**
 * Показать элемент
 */
fun <T> Collection<VisibleData<T>>.setVisible(value: T) {
    this
            .find { it.data == value }
            .apply { this!!.visible() }
}

/**
 * Скрыть элемент
 */
fun <T> Collection<VisibleData<T>>.setInvisible(value: T) {
    this
            .find { it.data == value }
            .apply { this!!.invisible() }
}

/**
 * Показать элемент, используя предикат
 */
fun <T> Collection<VisibleData<T>>.setVisible(predicate: (T) -> Boolean) {
    this
            .find { predicate(it.data) }
            .apply { this!!.visible() }
}

/**
 * Скрыть элемент, используя предикат
 */
fun <T> Collection<VisibleData<T>>.setInvisible(predicate: (T) -> Boolean) {
    this
            .find { predicate(it.data) }
            .apply { this!!.invisible() }
}