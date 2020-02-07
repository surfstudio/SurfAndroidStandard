package ru.surfstudio.navigation.navigator.backstack.fragment

import androidx.fragment.app.Fragment
import ru.surfstudio.navigation.navigator.backstack.BackStack
import ru.surfstudio.navigation.navigator.backstack.fragment.entry.FragmentBackStackEntry
import java.util.Stack

/**
 * Бекстек для фрагментов
 */
class FragmentBackStack : BackStack<FragmentBackStackEntry>, Stack<FragmentBackStackEntry>() {

    /**
     * Поиск записи в списке
     */
    fun find(tag: String): FragmentBackStackEntry? = find { it.tag == tag }

    /**
     * Поиск фрагмента в стеке
     */
    fun findFragment(tag: String): Fragment? = find(tag)?.fragment

    /**
     * Безопасное извлечение послендего фрагмента из стека. Если стек пуст - возвращается null.
     */
    fun peekFragment(): Fragment? = lastOrNull()?.fragment
}
