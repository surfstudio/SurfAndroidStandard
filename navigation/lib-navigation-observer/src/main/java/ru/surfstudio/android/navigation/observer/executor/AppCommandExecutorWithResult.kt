package ru.surfstudio.android.navigation.observer.executor

import ru.surfstudio.android.navigation.executor.AppCommandExecutor
import ru.surfstudio.android.navigation.executor.screen.activity.ActivityCommandExecutor
import ru.surfstudio.android.navigation.executor.screen.dialog.DialogCommandExecutor
import ru.surfstudio.android.navigation.executor.screen.fragment.FragmentCommandExecutor
import ru.surfstudio.android.navigation.observer.ScreenResultEmitter
import ru.surfstudio.android.navigation.observer.command.EmitScreenResult
import ru.surfstudio.android.navigation.command.NavigationCommand
import ru.surfstudio.android.navigation.provider.ActivityNavigationProvider

/**
 * [AppCommandExecutor] implementation, that supports [EmitScreenResult] command execution.
 */
class AppCommandExecutorWithResult(
        protected val screenResultEmitter: ScreenResultEmitter,
        activityNavigationProvider: ActivityNavigationProvider,
        activityCommandExecutor: ActivityCommandExecutor = ActivityCommandExecutor(activityNavigationProvider),
        fragmentCommandExecutor: FragmentCommandExecutor = FragmentCommandExecutor(activityNavigationProvider),
        dialogCommandExecutor: DialogCommandExecutor = DialogCommandExecutor(activityNavigationProvider)
) : AppCommandExecutor(activityNavigationProvider, activityCommandExecutor, fragmentCommandExecutor, dialogCommandExecutor) {

    private val screenResultDispatcher = ScreenResultDispatcher()

    override fun dispatchCommand(command: NavigationCommand) {
        if (command is EmitScreenResult<*, *>) {
            screenResultDispatcher.dispatch(screenResultEmitter, command)
        } else {
            super.dispatchCommand(command)
        }
    }
}