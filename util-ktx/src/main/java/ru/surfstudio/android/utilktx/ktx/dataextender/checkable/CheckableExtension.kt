package com.company.uidata.dataextender.checkable

/**
 * Extension-функции для коллекции, использующая [CheckableData]
 * у [CheckableData] может быть несколько выделенных элементов
 */

/**
 * Поставить выделение для <T>
 */
fun <T> Collection<CheckableData<T>>.setCheck(value: T) {
    this
            .find { it.data == value }
            .apply { this!!.isChecked = true }
}

/**
 * Убрать выделение для <T>
 */
fun <T> Collection<CheckableData<T>>.setUncheck(value: T) {
    this
            .find { it.data == value }
            .apply { this!!.isChecked = false }
}

fun <T> Collection<CheckableData<T>>.setCheckLambda(valueLambda: (CheckableData<T>) -> Unit) {
    //todo
}

/**
 * @return коллекции с выделенными
 */
fun <T> Collection<CheckableData<T>>.getChecked(): Collection<T>? = this
        .filter { it.isChecked }
        .map { it.data }

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
    this.first().apply {
        this.isChecked = true
    }
}

/**
 * @return есть ли хотя бы один выделенный объект?
 */
fun <T> Collection<CheckableData<T>>.isAnyChecked(): Boolean
        = this.find { it.isChecked } != null
