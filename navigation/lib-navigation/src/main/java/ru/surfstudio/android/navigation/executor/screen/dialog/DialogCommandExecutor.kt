package ru.surfstudio.android.navigation.executor.screen.dialog

import ru.surfstudio.android.navigation.command.dialog.Dismiss
import ru.surfstudio.android.navigation.command.dialog.Show
import ru.surfstudio.android.navigation.command.dialog.base.DialogNavigationCommand
import ru.surfstudio.android.navigation.provider.ActivityNavigationProvider
import ru.surfstudio.android.navigation.executor.CommandExecutor
import ru.surfstudio.android.navigation.navigator.dialog.DialogNavigatorInterface

open class DialogCommandExecutor(
        private val activityNavigationProvider: ActivityNavigationProvider
) : CommandExecutor<DialogNavigationCommand> {

    private val dialogNavigator: DialogNavigatorInterface
        get() = activityNavigationProvider.provide().dialogNavigator

    override fun execute(command: DialogNavigationCommand) {
        when (command) {
            is Show -> dialogNavigator.show(command.route)
            is Dismiss -> dialogNavigator.dismiss(command.route)
        }
    }

    override fun execute(commands: List<DialogNavigationCommand>) {
        TODO("Dialog navigation command list execution")
    }
}