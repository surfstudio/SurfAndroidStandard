package ru.surfstudio.android.navigation.executor.screen.activity

import ru.surfstudio.android.navigation.command.activity.*
import ru.surfstudio.android.navigation.command.activity.base.ActivityNavigationCommand
import ru.surfstudio.android.navigation.provider.ActivityNavigationProvider
import ru.surfstudio.android.navigation.executor.CommandExecutor
import ru.surfstudio.android.navigation.navigator.activity.ActivityNavigatorInterface

/**
 * Command executor for [ActivityNavigationCommand]s.
 *
 * There should be single instance of this executor for an application.
 */
open class ActivityCommandExecutor(
        private val activityNavigationProvider: ActivityNavigationProvider
) : CommandExecutor<ActivityNavigationCommand> {

    private val navigator: ActivityNavigatorInterface
        get() = activityNavigationProvider.provide().activityNavigator

    override fun execute(command: ActivityNavigationCommand) {
        when (command) {
            is Start -> navigator.start(command.route, command.animations, command.activityOptions)
            is Replace -> navigator.replace(command.route, command.animations, command.activityOptions)
            is Finish -> navigator.finish()
            is FinishAffinity -> navigator.finishAffinity()
        }
    }

    override fun execute(commands: List<ActivityNavigationCommand>) {
        if (commands.isEmpty()) return

        val lastCommand = commands.first() as Start
        navigator.start(
                routes = commands.map { it.route },
                animations = lastCommand.animations,
                activityOptions = lastCommand.activityOptions
        )
    }
}