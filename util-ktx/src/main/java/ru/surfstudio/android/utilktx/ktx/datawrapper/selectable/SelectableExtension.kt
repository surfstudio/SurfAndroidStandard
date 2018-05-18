package ru.surfstudio.android.utilktx.ktx.datawrapper.selectable

import ru.surfstudio.android.utilktx.ktx.datawrapper.findAndApply

/**
 * Extension-функции для коллекции, использующая [SelectableData]
 * у [SelectableData] может быть выделен только один элемент
 *
 * Если необходимо множественное выделение -> смотри [CheckableData]
 */

/**
 * Поставить выделение для <T>, используя предикат
 */
fun <T> Collection<SelectableData<T>>.setSelected(predicate: (T) -> Boolean) {
    setUnselected()
    findAndApply(this, { predicate(it) }, { it.isSelected = true })
}

/**
 * Поставить выделение для <T>
 */
fun <T> Collection<SelectableData<T>>.setSelected(value: T) {
    setSelected(predicate = { it == value })
}

/**
 * @return возвращает выделенный объект <T>. Если ни один не выделе, то null
 */
fun <T> Collection<SelectableData<T>>.getSelectedData(): T? =
        this.find { it.isSelected }?.data

/**
 * @return возвращает выделенный объект SelectableData<T>. Если ни один не выделе, то null
 */
fun <T> Collection<SelectableData<T>>.getSelected(): SelectableData<T>? =
        this.find { it.isSelected }

/**
 * убирает выделение у всей коллекции
 */
fun <T> Collection<SelectableData<T>>.setUnselected() {
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
    setUnselected()
    this.first().apply {
        this.isSelected = true
    }
}

/**
 * @return есть ли выделенный объект?
 */
fun <T> Collection<SelectableData<T>>.isAnySelected(): Boolean =
        this.find { it.isSelected } != null