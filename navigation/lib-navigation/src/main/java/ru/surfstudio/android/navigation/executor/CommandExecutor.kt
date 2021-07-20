package ru.surfstudio.android.navigation.executor

import ru.surfstudio.android.navigation.command.NavigationCommand

/**
 * Class which represents a base executioner interface.
 *
 * It can execute one or several navigation commands.
 */
interface CommandExecutor<C : NavigationCommand> {

    fun execute(command: C)

    fun execute(commands: List<C>)
}