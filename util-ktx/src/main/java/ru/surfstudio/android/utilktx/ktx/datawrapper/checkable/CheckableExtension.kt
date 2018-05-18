package ru.surfstudio.android.utilktx.ktx.datawrapper.checkable

import ru.surfstudio.android.utilktx.ktx.datawrapper.findAndApply

/**
 * Extension-функции для коллекции, использующая [CheckableData]
 * у [CheckableData] может быть несколько выделенных элементов
 *
 * Если необходимо одиночное выделение -> смотри [SelectableData]
 */

/**
 * Поставить выделение для <T>, удовлетворающее предикату
 *
 * @param (T) -> Boolean
 */
fun <T> Collection<CheckableData<T>>.setCheck(predicate: (T) -> Boolean) {
    findAndApply(this, { predicate(it) }, { it.isChecked = true })
}

/**
 * Убрать выделение для <T>, удовлетворающее предикату
 *
 * @param (T) -> Boolean
 */
fun <T> Collection<CheckableData<T>>.setUncheck(predicate: (T) -> Boolean) {
    findAndApply(this, { predicate(it) }, { it.isChecked = false })
}

/**
 * Поставить выделение для <T>
 * @param T
 */
fun <T> Collection<CheckableData<T>>.setCheck(value: T) {
    setCheck(predicate = { it == value })
}

/**
 * Убрать выделение для <T>
 */
fun <T> Collection<CheckableData<T>>.setUncheck(value: T) {
    setUncheck(predicate = { it == value })
}

/**
 * @return коллекции с выделенными
 */
fun <T> Collection<CheckableData<T>>.getChecked(): Collection<T>? =
        this.filter { it.isChecked }.map { it.data }

/**
 * Выделить все
 */
fun <T> Collection<CheckableData<T>>.setCheckedAll() {
    this.forEach {
        it.apply {
            this.isChecked = true
        }
    }
}

/**
 * Убрать выделение у всех
 */
fun <T> Collection<CheckableData<T>>.setUncheckedAll() {
    this.forEach {
        it.apply {
            this.isChecked = false
        }
    }
}

/**
 * Поставить выделение для первого
 */
fun <T> Collection<CheckableData<T>>.setCheckedFirst() {
    this.first().apply { this.isChecked = true }
}

/**
 * @return есть ли хотя бы один выделенный объект?
 */
fun <T> Collection<CheckableData<T>>.isAnyChecked(): Boolean =
        this.find { it.isChecked } != null
