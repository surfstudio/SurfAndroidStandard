package ru.surfstudio.android.navigation.executor.screen.dialog

import ru.surfstudio.android.navigation.command.activity.base.ActivityNavigationCommand
import ru.surfstudio.android.navigation.command.dialog.Dismiss
import ru.surfstudio.android.navigation.command.dialog.Show
import ru.surfstudio.android.navigation.command.dialog.base.DialogNavigationCommand
import ru.surfstudio.android.navigation.executor.CommandExecutor
import ru.surfstudio.android.navigation.navigator.dialog.DialogNavigatorInterface
import ru.surfstudio.android.navigation.provider.ActivityNavigationProvider

/**
 * Command executor for [ActivityNavigationCommand]s.
 *
 * There should be single instance of this executor for an application.
 */
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