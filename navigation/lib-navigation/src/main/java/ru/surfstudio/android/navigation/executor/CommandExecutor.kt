package ru.surfstudio.android.navigation.executor

import ru.surfstudio.android.navigation.command.NavigationCommand

interface CommandExecutor<T: NavigationCommand> {

    fun execute(command: T)
}