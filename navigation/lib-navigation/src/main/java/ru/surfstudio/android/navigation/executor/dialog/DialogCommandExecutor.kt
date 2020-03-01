package ru.surfstudio.android.navigation.executor.dialog

import ru.surfstudio.android.navigation.command.dialog.Dismiss
import ru.surfstudio.android.navigation.command.dialog.Show
import ru.surfstudio.android.navigation.command.dialog.base.DialogNavigationCommand
import ru.surfstudio.android.navigation.executor.CommandExecutor

open class DialogCommandExecutor(
) : CommandExecutor<DialogNavigationCommand> {

    open override fun execute(command: DialogNavigationCommand) {
        when (command) {
            is Show -> TODO()
            is Dismiss -> TODO()
        }
    }
}