package ru.surfstudio.android.utilktx.ktx.dataextender.scrollable

/**
 * Extension-функции для коллекции, использующая [ScrollableData]
 */

/**
 * Получить позицию списка у внутреннего элемента value
 */
fun <T> Collection<ScrollableData<T>>.getCurrentScrollPosition(value: T): Int? =
        this.find { it.data == value }?.scrollPosition

/**
 * Получить позицию списка у внутреннего элемента value,
 * используя предикат
 */
fun <T> Collection<ScrollableData<T>>.getCurrentScrollPosition(predicate: (T) -> Boolean): Int? =
        this.find { predicate(it.data) }?.scrollPosition
