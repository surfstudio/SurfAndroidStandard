package ru.surfstudio.android.navigation.command.fragment.base

import ru.surfstudio.android.navigation.command.NavigationCommand
import ru.surfstudio.android.navigation.route.fragment.FragmentRoute

interface FragmentNavigationCommand: NavigationCommand {
    override val route: FragmentRoute
}