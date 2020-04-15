package ru.surfstudio.android.navigation.executor.dialog

import ru.surfstudio.android.navigation.command.dialog.Dismiss
import ru.surfstudio.android.navigation.command.dialog.Show
import ru.surfstudio.android.navigation.command.dialog.base.DialogNavigationCommand
import ru.surfstudio.android.navigation.di.supplier.ActivityNavigationSupplier
import ru.surfstudio.android.navigation.executor.CommandExecutor
import ru.surfstudio.android.navigation.navigator.dialog.DialogNavigatorInterface

open class DialogCommandExecutor(
        private val activityNavigationSupplier: ActivityNavigationSupplier
) : CommandExecutor<DialogNavigationCommand> {

    private val dialogNavigator: DialogNavigatorInterface
        get() = activityNavigationSupplier.obtain().dialogNavigator

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