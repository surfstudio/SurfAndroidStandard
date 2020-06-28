package ru.surfstudio.android.navigation.sample_standard.screen.base.command

import ru.surfstudio.android.navigation.command.NavigationCommand
import ru.surfstudio.android.navigation.executor.NavigationCommandExecutor

interface CommandExecutionPresenter {
    val commandExecutor: NavigationCommandExecutor

    fun NavigationCommand.execute() = commandExecutor.execute(this)

    fun List<NavigationCommand>.execute() = commandExecutor.execute(this)
}