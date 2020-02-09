package ru.surfstudio.android.navigation.navigator.backstack

/**
 * Base back stack model.
 * It works on a Stack-based interface and contains entries with type [E]
 */
interface BackStack<E : BackStack.Entry> {

    fun push(entry: E): E

    fun pop(): E

    fun peek(): E

    val size: Int

    /**
     * Base back stack entry.
     */
    interface Entry
}
