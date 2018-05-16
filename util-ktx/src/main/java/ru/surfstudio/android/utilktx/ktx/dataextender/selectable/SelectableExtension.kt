package com.company.uidata.dataextender.selectable

/**
 * Extension-функции для коллекции, использующая [SelectableData]
 * у [SelectableData] может быть выделен только один элемент
 */

/**
 * Поставить выделение для <T>
 */
fun <T> Collection<SelectableData<T>>.setSelected(value: T) {
    val selected = this.getSelected()
    if (selected != null) {
        selected.isSelected = false
    }

    this
            .find { it.data == value}
            .apply { this!!.isSelected = true }
}

fun <T> Collection<SelectableData<T>>.setSelectedLambda(valueLambda: (SelectableData<T>) -> Unit) {
    //todo
}

/**
 * @return возвращает выделенный объект <T>. Если ни один не выделе, то null
 */
fun <T> Collection<SelectableData<T>>.getSelectedData(): T?
        = this.find { it.isSelected }?.data

/**
 * @return возвращает выделенный объект SelectableData<T>. Если ни один не выделе, то null
 */
fun <T> Collection<SelectableData<T>>.getSelected(): SelectableData<T>?
        = this.find { it.isSelected }

/**
 * убирает выделение у всей коллекции
 */
fun <T> Collection<SelectableData<T>>.setUnselectedAll() {
    this.forEach {
        it.apply {
            this.isSelected = false
        }
    }
}

/**
 * ставит выделение на первый элемент
 */
fun <T> Collection<SelectableData<T>>.setSelectedFirst() {
    this.setUnselectedAll()
    this.first().apply {
        this.isSelected = true
    }
}

/**
 * @return есть ли выделенный объект?
 */
fun <T> Collection<SelectableData<T>>.isAnySelected(): Boolean
        = this.find { it.isSelected } != null