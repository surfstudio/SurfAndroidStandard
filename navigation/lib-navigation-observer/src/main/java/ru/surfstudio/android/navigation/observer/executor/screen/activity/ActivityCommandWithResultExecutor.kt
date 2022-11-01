package ru.surfstudio.android.navigation.observer.executor.screen.activity

import ru.surfstudio.android.navigation.command.activity.base.ActivityNavigationCommand
import ru.surfstudio.android.navigation.executor.screen.activity.ActivityCommandExecutor
import ru.surfstudio.android.navigation.observer.command.activity.RequestPermission
import ru.surfstudio.android.navigation.observer.command.activity.StartForResult
import ru.surfstudio.android.navigation.observer.navigator.activity.ActivityNavigatorWithResult
import ru.surfstudio.android.navigation.observer.route.ActivityWithResultRoute
import ru.surfstudio.android.navigation.provider.ActivityNavigationProvider
import java.io.Serializable

/**
 * This executor supports command [StartForResult] and all commands of its parent.
 * @see [ActivityCommandExecutor]
 */
class ActivityCommandWithResultExecutor(
    private val activityNavigationProvider: ActivityNavigationProvider
) : ActivityCommandExecutor(activityNavigationProvider) {

    private val navigatorWithResult: ActivityNavigatorWithResult
        get() = activityNavigationProvider.provide().activityNavigator as ActivityNavigatorWithResult

    @Suppress("UNCHECKED_CAST")
    override fun execute(command: ActivityNavigationCommand) {
        when (command) {
            is StartForResult<*, *> -> {
                val castedCommand =
                    command as StartForResult<Serializable, ActivityWithResultRoute<Serializable>>
                startForResult(castedCommand)
            }
            is RequestPermission -> {
                navigatorWithResult.requestPermission(command.route)
            }
            else -> super.execute(command)
        }
    }

    private fun <T : Serializable, R : ActivityWithResultRoute<T>> startForResult(command: StartForResult<T, R>) {
        navigatorWithResult.startForResult(
            command.route,
            command.animations,
            command.activityOptions
        )
    }
}
