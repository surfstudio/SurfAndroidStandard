package ru.surfstudio.android.navigation.executor

import ru.surfstudio.android.navigation.command.NavigationCommand

/**
 * [CommandExecutor] which supports any type of [NavigationCommand].
 */
interface NavigationCommandExecutor : CommandExecutor<NavigationCommand>