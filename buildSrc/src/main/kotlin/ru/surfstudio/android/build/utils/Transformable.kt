package ru.surfstudio.android.build.utils

/**
 * Help transform one object to another
 *
 * @param T - target object type
 */
interface Transformable<T> {

    fun transform(): T
}