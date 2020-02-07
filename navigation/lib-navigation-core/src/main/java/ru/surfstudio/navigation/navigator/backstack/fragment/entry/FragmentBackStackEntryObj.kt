package ru.surfstudio.navigation.navigator.backstack.fragment.entry

import ru.surfstudio.navigation.command.NavigationCommand
import java.io.Serializable

/**
 * Сериализуемая модель [FragmentBackStackEntry], которая переживает смену конфигурации
 */
data class FragmentBackStackEntryObj(
        val tag: String,
        val command: NavigationCommand
) : Serializable {

    companion object {

        fun from(entry: FragmentBackStackEntry): FragmentBackStackEntryObj =
                FragmentBackStackEntryObj(entry.tag, entry.command)
    }
}