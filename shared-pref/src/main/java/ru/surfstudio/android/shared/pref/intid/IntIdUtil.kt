package ru.surfstudio.android.shared.pref.intid


import java.lang.IllegalStateException
import java.util.*

/**
 * Позволяет из коллекции с элементами, имплементирующими IntId, получить
 * элемент по совпадающему значению
 * Изначально создано для поиска значения Enum
 */

object IntIdUtil {

    fun <T : IntId> getByValue(values: Array<T>, value: Int): T {
        values
                .filter { it.id() == value }
                .forEach { return it }
        throw IllegalStateException(String.format("Not found id: %s in values: %s with ids %s",
                value, Arrays.toString(values), ids(values)))
    }

    private fun <T : IntId> ids(values: Array<T>): List<Int> {
        val result = ArrayList<Int>(values.size)
        values.mapTo(result) { it.id() }
        return result
    }
}
