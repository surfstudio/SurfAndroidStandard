package ru.surfstudio.android.navigation.navigator.backstack.fragment.entry

import androidx.fragment.app.Fragment
import ru.surfstudio.android.navigation.command.NavigationCommand
import ru.surfstudio.android.navigation.navigator.backstack.BackStack

/**
 * Fragment back stack entry
 *
 * @param tag tag used to add fragment to a transaction
 * @param fragment added fragment
 * @param command command which was used to add fragment
 */
data class FragmentBackStackEntry(
    val tag: String,
    val fragment: Fragment,
    val command: NavigationCommand
) : BackStack.Entry
