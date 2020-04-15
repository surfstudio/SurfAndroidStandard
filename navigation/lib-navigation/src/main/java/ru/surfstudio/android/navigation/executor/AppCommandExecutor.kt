package ru.surfstudio.android.navigation.executor

import ru.surfstudio.android.navigation.command.NavigationCommand
import ru.surfstudio.android.navigation.command.activity.base.ActivityNavigationCommand
import ru.surfstudio.android.navigation.command.dialog.base.DialogNavigationCommand
import ru.surfstudio.android.navigation.command.fragment.base.FragmentNavigationCommand
import ru.surfstudio.android.navigation.di.supplier.ActivityNavigationSupplier
import ru.surfstudio.android.navigation.executor.activity.ActivityCommandExecutor
import ru.surfstudio.android.navigation.executor.dialog.DialogCommandExecutor
import ru.surfstudio.android.navigation.executor.fragment.FragmentCommandExecutor

open class AppCommandExecutor(
        private val activityNavigationSupplier: ActivityNavigationSupplier,
        private val activityCommandExecutor: ActivityCommandExecutor,
        private val fragmentCommandExecutor: FragmentCommandExecutor,
        private val dialogCommandExecutor: DialogCommandExecutor
) : CommandExecutor<NavigationCommand> {

    protected val buffer = mutableListOf<NavigationCommand>()

    override fun execute(command: NavigationCommand) {
        execute(listOf(command))
    }

    override fun execute(commands: List<NavigationCommand>) {
        if (activityNavigationSupplier.hasCurrentHolder()) {
            commands.forEach(::executeInternal) //TODO think about synchronous execution
        } else {
            activityNavigationSupplier.setOnHolderActiveListenerSingle { executeBuffer() }
        }
    }

    private fun executeBuffer() {
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