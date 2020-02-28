package ru.surfstudio.android.utilktx.data.wrapper.visible

import ru.surfstudio.android.utilktx.data.wrapper.DataWrapperInterface
import ru.surfstudio.android.utilktx.data.wrapper.filterAndApply

/**
 * Extension-функции для коллекции, использующая [VisibleData]
 */

/**
 * Изменить состояние элемента на противоположный, используя предикат
 */
fun <T, E> Collection<E>.toggleVisibility(predicate: (T) -> Boolean)
        where E : DataWrapperInterface<T>, E : VisibleDataInterface {
    filterAndApply(this, { predicate(it) }, { it.toggleVisibility() })
}

/**
 * Показать элемент, используя предикат
 */
fun <T, E> Collection<E>.show(predicate: (T) -> Boolean)
        where E : DataWrapperInterface<T>, E : VisibleDataInterface {
    filterAndApply(this, { predicate(it) }, { it.show() })
}

/**
 * Скрыть элемент, используя предикат
 */
fun <T, E> Collection<E>.hide(predicate: (T) -> Boolean)
        where E : DataWrapperInterface<T>, E : VisibleDataInterface {
    filterAndApply(this, { predicate(it) }, { it.hide() })
}

/**
 * Изменить состояние элемента на противоположный
 */
fun <T, E> Collection<E>.toggleVisibility(value: T)
        where E : DataWrapperInterface<T>, E : VisibleDataInterface {
    toggleVisibility(predicate = { it == value })
}

/**
 * Показать элемент
 */
fun <T, E> Collection<E>.show(value: T)
        where E : DataWrapperInterface<T>, E : VisibleDataInterface {
    show(predicate = { it == value })
}

/**
 * Скрыть элемент
 */
fun <T, E> Collection<E>.hide(value: T)
        where E : DataWrapperInterface<T>, E : VisibleDataInterface {
    hide(predicate = { it == value })
}
