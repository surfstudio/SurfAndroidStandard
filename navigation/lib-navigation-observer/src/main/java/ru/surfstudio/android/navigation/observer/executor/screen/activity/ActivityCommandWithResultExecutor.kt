package ru.surfstudio.android.navigation.observer.executor.screen.activity

import ru.surfstudio.android.navigation.command.activity.base.ActivityNavigationCommand
import ru.surfstudio.android.navigation.executor.screen.activity.ActivityCommandExecutor
import ru.surfstudio.android.navigation.observer.ScreenResultEmitter
import ru.surfstudio.android.navigation.observer.command.EmitScreenResult
import ru.surfstudio.android.navigation.observer.command.activity.StartForResult
import ru.surfstudio.android.navigation.observer.executor.ScreenResultDispatcher
import ru.surfstudio.android.navigation.observer.navigator.activity.ActivityNavigatorWithResultInterface
import ru.surfstudio.android.navigation.observer.route.ActivityWithResultRoute
import ru.surfstudio.android.navigation.provider.ActivityNavigationProvider
import java.io.Serializable

class ActivityCommandWithResultExecutor(
        private val activityNavigationProvider: ActivityNavigationProvider,
        private val screenResultEmitter: ScreenResultEmitter
) : ActivityCommandExecutor(activityNavigationProvider) {

    private val screenResultDispatcher = ScreenResultDispatcher()

    override fun execute(command: ActivityNavigationCommand) {
        when (command) {
            is StartForResult<*, *> -> {
                val castedCommand = command as StartForResult<Serializable, ActivityWithResultRoute<Serializable>>
                listenForResult(castedCommand)
                startForResult(castedCommand)
            }
            else -> super.execute(command)
        }
    }

    private fun <T : Serializable, R : ActivityWithResultRoute<T>> listenForResult(command: StartForResult<T, R>) {
        (activityNavigationProvider.provide().activityNavigator as ActivityNavigatorWithResultInterface)
                .callbackResult(command.route) { data ->
                    val resultCommand = EmitScreenResult(command.route, data)
                    screenResultDispatcher.dispatch(screenResultEmitter, resultCommand)
                }
    }

    private fun <T : Serializable, R : ActivityWithResultRoute<T>> startForResult(command: StartForResult<T, R>) {
        (activityNavigationProvider.provide().activityNavigator as ActivityNavigatorWithResultInterface)
                .startForResult(command.route, command.animations, command.activityOptions)
    }
}