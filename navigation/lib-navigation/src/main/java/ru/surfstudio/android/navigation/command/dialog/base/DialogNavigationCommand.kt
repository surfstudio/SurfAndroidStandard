package ru.surfstudio.android.navigation.command.dialog.base

import ru.surfstudio.android.navigation.command.NavigationCommand
import ru.surfstudio.android.navigation.route.Route
import ru.surfstudio.android.navigation.route.dialog.DialogRoute

interface DialogNavigationCommand : NavigationCommand {
    override val route: DialogRoute
}