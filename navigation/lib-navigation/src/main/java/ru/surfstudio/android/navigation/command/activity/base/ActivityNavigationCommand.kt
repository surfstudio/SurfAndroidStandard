package ru.surfstudio.android.navigation.command.activity.base

import ru.surfstudio.android.navigation.command.NavigationCommand
import ru.surfstudio.android.navigation.route.activity.ActivityRoute

interface ActivityNavigationCommand: NavigationCommand {
    override val route: ActivityRoute
}