package ru.surfstudio.navigation.navigator.backstack.fragment.entry

import androidx.fragment.app.Fragment
import ru.surfstudio.navigation.command.NavigationCommand
import ru.surfstudio.navigation.navigator.backstack.BackStack

/**
 * Запись в бекстек для фрагментов
 *
 * @param tag тег, по которому фрагмент был добавлен
 * @param fragment добавленный фрагмент
 * @param command осуществляемая операция
 */
data class FragmentBackStackEntry(
    val tag: String,
    val fragment: Fragment,
    val command: NavigationCommand
) : BackStack.Entry
