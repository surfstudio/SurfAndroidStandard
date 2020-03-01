package ru.surfstudio.android.navigation.executor

import ru.surfstudio.android.navigation.command.NavigationCommand

interface CommandExecutor<C: NavigationCommand> {

    fun execute(command: C)
}