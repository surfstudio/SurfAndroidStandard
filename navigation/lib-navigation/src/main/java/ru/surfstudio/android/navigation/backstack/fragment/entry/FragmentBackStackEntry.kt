package ru.surfstudio.android.navigation.backstack.fragment.entry

import androidx.fragment.app.Fragment
import ru.surfstudio.android.navigation.backstack.BackStack
import ru.surfstudio.android.navigation.command.NavigationCommand

/**
 * Fragment back stack entry.
 *
 * This entry is contains useful information about fragment navigation command
 * and doesn't survive configuration changes.
 *
 * @param tag tag used to add fragment to a transaction
 * @param fragment added fragment
 * @param command command which was used to add fragment
 */
data class FragmentBackStackEntry(
        val tag: String,
        val fragment: Fragment,
        val command: NavigationCommand
)
