package ru.surfstudio.android.navigation.executor.activity

import ru.surfstudio.android.navigation.command.activity.*
import ru.surfstudio.android.navigation.command.activity.base.ActivityNavigationCommand
import ru.surfstudio.android.navigation.executor.CommandExecutor
import ru.surfstudio.android.navigation.navigator.activity.ActivityNavigator

open class ActivityCommandExecutor(
        private val activityNavigator: ActivityNavigator
): CommandExecutor<ActivityNavigationCommand> {

    open override fun execute(command: ActivityNavigationCommand) {
        when (command) {
            is Start -> activityNavigator.start(command.route, command.animations, command.activityOptions)
            is Replace -> activityNavigator.replace(command.route, command.animations, command.activityOptions)
            is Finish -> activityNavigator.finish()
            is FinishAffinity -> activityNavigator.finishAffinity()
        }
    }
}