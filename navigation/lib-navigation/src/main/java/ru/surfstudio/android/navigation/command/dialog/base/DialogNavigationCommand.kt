package ru.surfstudio.android.navigation.command.dialog.base

import ru.surfstudio.android.navigation.command.NavigationCommand
import ru.surfstudio.android.navigation.route.dialog.DialogRoute

/**
 * Command that is used in navigation process with [androidx.fragment.app.DialogFragment].
 */
interface DialogNavigationCommand : NavigationCommand {
    override val route: DialogRoute
}