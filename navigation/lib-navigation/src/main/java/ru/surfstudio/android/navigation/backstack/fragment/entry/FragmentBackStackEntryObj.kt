package ru.surfstudio.android.navigation.backstack.fragment.entry

import androidx.fragment.app.Fragment
import ru.surfstudio.android.navigation.command.NavigationCommand
import java.io.Serializable

/**
 * Serializable [FragmentBackStackEntry] model which survives configuration changes.
 */
data class FragmentBackStackEntryObj(
        val tag: String,
        val command: NavigationCommand
) : Serializable {

    /**
     * Transforms this serializable model to data model with [fragment] obtained from FragmentManager.
     */
    fun toEntry(fragment: Fragment): FragmentBackStackEntry {
        return FragmentBackStackEntry(tag, fragment, command)
    }

    companion object {

        /**
         * Factory method to create [FragmentBackStackEntryObj] from [FragmentBackStackEntry].
         */
        fun from(entry: FragmentBackStackEntry): FragmentBackStackEntryObj =
                FragmentBackStackEntryObj(entry.tag, entry.command)
    }
}