package ru.surfstudio.android.navigation.executor.screen.activity

import ru.surfstudio.android.navigation.command.activity.*
import ru.surfstudio.android.navigation.command.activity.base.ActivityNavigationCommand
import ru.surfstudio.android.navigation.provider.ActivityNavigationProvider
import ru.surfstudio.android.navigation.executor.CommandExecutor

open class ActivityCommandExecutor(
        private val activityNavigationProvider: ActivityNavigationProvider
) : CommandExecutor<ActivityNavigationCommand> {

    override fun execute(command: ActivityNavigationCommand) {
        val navigator = activityNavigationProvider.provide().activityNavigator
        when (command) {
            is Start -> navigator.start(command.route, command.animations, command.activityOptions)
            is Replace -> navigator.replace(command.route, command.animations, command.activityOptions)
            is Finish -> navigator.finish()
            is FinishAffinity -> navigator.finishAffinity()
        }
    }

    override fun execute(commands: List<ActivityNavigationCommand>) {
        TODO("Activity navigation command list execution")
    }
}