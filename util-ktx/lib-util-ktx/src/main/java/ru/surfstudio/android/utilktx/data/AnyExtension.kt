package ru.surfstudio.android.utilktx.data

/**
 * Производит проверку `this` на принадлежность к любому из `items`.
 *
 * @return `true` -> если имеется хоть одно совпадение `this.equals(that)`.
 * */
fun Any.isOneOf(vararg items: Any): Boolean {
    return items.any(::equals)
}
