package ru.surfstudio.android.navigation.executor

import ru.surfstudio.android.navigation.command.NavigationCommand
import ru.surfstudio.android.navigation.command.activity.base.ActivityNavigationCommand
import ru.surfstudio.android.navigation.command.dialog.base.DialogNavigationCommand
import ru.surfstudio.android.navigation.command.fragment.base.FragmentNavigationCommand
import ru.surfstudio.android.navigation.provider.ActivityNavigationProvider
import ru.surfstudio.android.navigation.executor.screen.activity.ActivityCommandExecutor
import ru.surfstudio.android.navigation.executor.screen.dialog.DialogCommandExecutor
import ru.surfstudio.android.navigation.executor.screen.fragment.FragmentCommandExecutor

/**
 * Command
 */
//@PerApp
open class AppCommandExecutor(
        protected val activityNavigationProvider: ActivityNavigationProvider,
        protected val activityCommandExecutor: ActivityCommandExecutor = ActivityCommandExecutor(activityNavigationProvider),
        protected val fragmentCommandExecutor: FragmentCommandExecutor = FragmentCommandExecutor(activityNavigationProvider),
        protected val dialogCommandExecutor: DialogCommandExecutor = DialogCommandExecutor(activityNavigationProvider)
) : NavigationCommandExecutor {

    protected val buffer = mutableListOf<NavigationCommand>()

    override fun execute(command: NavigationCommand) {
        execute(listOf(command))
    }

    override fun execute(commands: List<NavigationCommand>) {
        if (activityNavigationProvider.hasCurrentHolder()) {
            /* TODO: Think about smart command execution:
            * For example, if fragment command follows activity command,
            * we must wait for activity command to be fully executed,
            * i.e. next activity will become fully visible to execute fragment navigation command.
            * */
            commands.forEach(::executeInternal)
        } else {
            buffer.addAll(commands)
            activityNavigationProvider.setOnHolderActiveListenerSingle { executeBuffer() }
        }
    }

    protected fun executeBuffer() {
        execute(buffer)
        buffer.clear()
    }

    protected fun executeInternal(command: NavigationCommand) {
        when (command) {
            is ActivityNavigationCommand -> activityCommandExecutor.execute(command)
            is FragmentNavigationCommand -> fragmentCommandExecutor.execute(command)
            is DialogNavigationCommand -> dialogCommandExecutor.execute(command)
        }
    }
}