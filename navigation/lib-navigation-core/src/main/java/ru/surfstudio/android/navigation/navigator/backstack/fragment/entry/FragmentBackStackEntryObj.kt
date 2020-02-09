package ru.surfstudio.android.navigation.navigator.backstack.fragment.entry

import ru.surfstudio.android.navigation.command.NavigationCommand
import java.io.Serializable

/**
 * Serializable [FragmentBackStackEntry] model which survives configuration changes
 */
data class FragmentBackStackEntryObj(
        val tag: String,
        val command: NavigationCommand
) : Serializable {

    companion object {

        /**
         * Factory method to create [FragmentBackStackEntryObj] from [FragmentBackStackEntry].
         */
        fun from(entry: FragmentBackStackEntry): FragmentBackStackEntryObj =
                FragmentBackStackEntryObj(entry.tag, entry.command)
    }
}