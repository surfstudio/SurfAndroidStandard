package ru.surfstudio.navigation.navigator.backstack

/**
 * Базовая модель бекстека, работающего на основе стека, и содержащая некие записи [Entry]
 */
interface BackStack<E : BackStack.Entry> {

    fun push(entry: E): E

    fun pop(): E

    fun peek(): E

    val size: Int

    /**
     * Запись в бекстек
     */
    interface Entry
}
