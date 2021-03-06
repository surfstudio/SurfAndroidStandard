package ru.surfstudio.android.navigation.observer.deprecated.executor.screen.activity

import ru.surfstudio.android.navigation.command.activity.base.ActivityNavigationCommand
import ru.surfstudio.android.navigation.executor.screen.activity.ActivityCommandExecutor
import ru.surfstudio.android.navigation.observer.deprecated.ScreenResultEmitter
import ru.surfstudio.android.navigation.observer.deprecated.command.EmitScreenResult
import ru.surfstudio.android.navigation.observer.deprecated.command.activity.StartForResult
import ru.surfstudio.android.navigation.observer.deprecated.executor.ScreenResultDispatcher
import ru.surfstudio.android.navigation.observer.deprecated.navigator.activity.ActivityNavigatorWithResult
import ru.surfstudio.android.navigation.observer.deprecated.route.ActivityWithResultRoute
import ru.surfstudio.android.navigation.provider.ActivityNavigationProvider
import java.io.Serializable

/**
 * This executor supports command [StartForResult] and all commands of its parent.
 * @see [ActivityCommandExecutor]
 */
@Deprecated("Prefer using new implementation")
class ActivityCommandWithResultExecutor(
        private val activityNavigationProvider: ActivityNavigationProvider,
        private val screenResultEmitter: ScreenResultEmitter
) : ActivityCommandExecutor(activityNavigationProvider) {

    private val screenResultDispatcher = ScreenResultDispatcher()
    private val navigatorWithResult: ActivityNavigatorWithResult
        get() = activityNavigationProvider.provide().activityNavigator as ActivityNavigatorWithResult

    @Suppress("UNCHECKED_CAST")
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
        navigatorWithResult.callbackResult(command.route) { data ->
            val emitResultCommand = EmitScreenResult(command.route, data)
            screenResultDispatcher.dispatch(screenResultEmitter, emitResultCommand)
        }
    }

    private fun <T : Serializable, R : ActivityWithResultRoute<T>> startForResult(command: StartForResult<T, R>) {
        navigatorWithResult.startForResult(command.route, command.animations, command.activityOptions)
    }
}