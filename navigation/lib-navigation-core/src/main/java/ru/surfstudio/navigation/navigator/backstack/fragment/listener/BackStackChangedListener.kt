package ru.surfstudio.navigation.navigator.backstack.fragment.listener

import ru.surfstudio.navigation.navigator.backstack.fragment.FragmentBackStack

/**
 * Лиснер на изменение бекстека фрагментов
 */
typealias BackStackChangedListener = (FragmentBackStack) -> Unit
