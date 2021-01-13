package ru.surfstudio.android.navigation.observer.executor

import ru.surfstudio.android.navigation.command.NavigationCommand
import ru.surfstudio.android.navigation.executor.AppCommandExecutor
import ru.surfstudio.android.navigation.executor.screen.activity.ActivityCommandExecutor
import ru.surfstudio.android.navigation.executor.screen.dialog.DialogCommandExecutor
import ru.surfstudio.android.navigation.executor.screen.fragment.FragmentCommandExecutor
import ru.surfstudio.android.navigation.observer.ScreenResultEmitter
import ru.surfstudio.android.navigation.observer.command.EmitScreenResult
import ru.surfstudio.android.navigation.observer.command.StartForResult
import ru.surfstudio.android.navigation.observer.navigator.activity.ActivityNavigatorWithResultInterface
import ru.surfstudio.android.navigation.observer.route.ActivityWithResultRoute
import ru.surfstudio.android.navigation.provider.ActivityNavigationProvider
import java.io.Serializable

/**
 * [AppCommandExecutor] implementation, that supports [EmitScreenResult] and [StartForResult] commands execution.
 */
open class AppCommandExecutorWithResult(
        protected val screenResultEmitter: ScreenResultEmitter,
        activityNavigationProvider: ActivityNavigationProvider,
        activityCommandExecutor: ActivityCommandExecutor = ActivityCommandExecutor(activityNavigationProvider),
        fragmentCommandExecutor: FragmentCommandExecutor = FragmentCommandExecutor(activityNavigationProvider),
        dialogCommandExecutor: DialogCommandExecutor = DialogCommandExecutor(activityNavigationProvider)
) : AppCommandExecutor(activityNavigationProvider, activityCommandExecutor, fragmentCommandExecutor, dialogCommandExecutor) {

    private val screenResultDispatcher = ScreenResultDispatcher()

    override fun dispatchCommand(command: NavigationCommand) {
        when (command) {
            is EmitScreenResult<*, *> -> {
                screenResultDispatcher.dispatch(screenResultEmitter, command)
            }
            is StartForResult<*, *> -> {
                val castedCommand = command as StartForResult<Serializable, ActivityWithResultRoute<Serializable>>
                listenForResult(castedCommand)
                startForResult(castedCommand)
            }
            else -> {
                super.dispatchCommand(command)
            }
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