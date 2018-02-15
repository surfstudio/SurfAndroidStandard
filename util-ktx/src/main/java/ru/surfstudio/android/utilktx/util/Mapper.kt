package ru.surfstudio.android.utilktx.util

/**
 * Задает отображение K -> V
 */
interface Mapper<K, V> {
    fun map(key: K): V
}