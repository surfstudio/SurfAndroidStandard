package ru.surfstudio.android.utilktx.data.wrapper.checkable

import ru.surfstudio.android.utilktx.data.wrapper.DataWrapperInterface
import ru.surfstudio.android.utilktx.data.wrapper.filterAndApply

/**
 * Extension-функции для коллекции, использующая [CheckableData]
 * у [CheckableData] может быть несколько выделенных элементов
 *
 * Если необходимо одиночное выделение -> смотри [SelectableData]
 */

/**
 * Изменить выделение для <T> на противоположный,
 * удовлетворающее предикату
 *
 * @param (T) -> Boolean
 */
fun <T, E> Collection<E>.toggleChecked(predicate: (T) -> Boolean)
        where E : DataWrapperInterface<T>, E : CheckableDataInterface {
    filterAndApply(this, { predicate(it) }, { it.toggleChecked() })
}

/**
 * Поставить выделение для <T>, удовлетворающее предикату
 *
 * @param (T) -> Boolean
 */
fun <T, E> Collection<E>.setCheck(predicate: (T) -> Boolean)
        where E : DataWrapperInterface<T>, E : CheckableDataInterface {
    filterAndApply(this, { predicate(it) }, { it.isChecked = true })
}

/**
 * Убрать выделение для <T>, удовлетворающее предикату
 *
 * @param (T) -> Boolean
 */
fun <T, E> Collection<E>.setUncheck(predicate: (T) -> Boolean)
        where E : DataWrapperInterface<T>, E : CheckableDataInterface {
    filterAndApply(this, { predicate(it) }, { it.isChecked = false })
}

/**
 * Изменить выделение для <T> на противоположный
 * @param T
 */
fun <T, E> Collection<E>.toggleChecked(value: T)
        where E : DataWrapperInterface<T>, E : CheckableDataInterface {
    toggleChecked(predicate = { it == value })
}

/**
 * Поставить выделение для <T>
 * @param T
 */
fun <T, E> Collection<E>.setCheck(value: T)
        where E : DataWrapperInterface<T>, E : CheckableDataInterface {
    setCheck(predicate = { it == value })
}

/**
 * Убрать выделение для <T>
 */
fun <T, E> Collection<E>.setUncheck(value: T)
        where E : DataWrapperInterface<T>, E : CheckableDataInterface {
    setUncheck(predicate = { it == value })
}

/**
 * @return коллекции с выделенными
 */
fun <T, E> Collection<E>.getChecked(): Collection<T>
        where E : DataWrapperInterface<T>, E : CheckableDataInterface {
    if (!isAnyChecked()) {
        throw IllegalStateException("нет выделенных элементов")
    }
    return this.filter { it.isChecked }.map { it.data }
}

/**
 * @return коллекции с выделенными. Если не найдено, то null
 */
fun <T, E> Collection<E>.getCheckedNullable(): Collection<T>?
        where E : DataWrapperInterface<T>, E : CheckableDataInterface =
        this.filter { it.isChecked }.map { it.data }

/**
 * Выделить все
 */
fun <T, E> Collection<E>.setCheckedAll()
        where E : DataWrapperInterface<T>, E : CheckableDataInterface {
    this.forEach {
        it.apply {
            this.isChecked = true
        }
    }
}

/**
 * Убрать выделение у всех
 */
fun <T, E> Collection<E>.setUncheckedAll()
        where E : DataWrapperInterface<T>, E : CheckableDataInterface {
    this.forEach {
        it.apply {
            this.isChecked = false
        }
    }
}

/**
 * Поставить выделение для первого
 */
fun <T, E> Collection<E>.setCheckedFirst()
        where E : DataWrapperInterface<T>, E : CheckableDataInterface {
    this.first().apply { this.isChecked = true }
}

/**
 * @return есть ли хотя бы один выделенный объект?
 */
fun <T, E> Collection<E>.isAnyChecked(): Boolean
        where E : DataWrapperInterface<T>, E : CheckableDataInterface =
        this.find { it.isChecked } != null
