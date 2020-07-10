package ru.surfstudio.android.navigation.executor

import android.os.Handler
import ru.surfstudio.android.navigation.command.NavigationCommand
import ru.surfstudio.android.navigation.command.activity.base.ActivityNavigationCommand
import ru.surfstudio.android.navigation.command.dialog.base.DialogNavigationCommand
import ru.surfstudio.android.navigation.command.fragment.base.FragmentNavigationCommand
import ru.surfstudio.android.navigation.provider.ActivityNavigationProvider
import ru.surfstudio.android.navigation.executor.screen.activity.ActivityCommandExecutor
import ru.surfstudio.android.navigation.executor.screen.dialog.DialogCommandExecutor
import ru.surfstudio.android.navigation.executor.screen.fragment.FragmentCommandExecutor
import java.util.*

/**
 * Command executor, which is used to distribute commands between typed nested executors.
 *
 * There should be single instance of this executor for an application.
 */
open class AppCommandExecutor(
        protected val activityNavigationProvider: ActivityNavigationProvider,
        protected val activityCommandExecutor: ActivityCommandExecutor = ActivityCommandExecutor(activityNavigationProvider),
        protected val fragmentCommandExecutor: FragmentCommandExecutor = FragmentCommandExecutor(activityNavigationProvider),
        protected val dialogCommandExecutor: DialogCommandExecutor = DialogCommandExecutor(activityNavigationProvider)
) : NavigationCommandExecutor {

    protected val buffer = mutableListOf<NavigationCommand>()
    protected val commandQueue: Queue<NavigationCommand> = LinkedList()

    override fun execute(command: NavigationCommand) {
        execute(listOf(command))
    }

    override fun execute(commands: List<NavigationCommand>) {
        safeExecuteWithBuffer(commands)
    }

    /**
     * Executes commands for current holder immediately if present,
     * or waits until any holder will become active.
     *
     * All commands, received in this waiting period will be transferred in buffer,
     * and executed once the holder is ready.
     */
    protected open fun safeExecuteWithBuffer(commands: List<NavigationCommand>) {
        if (activityNavigationProvider.hasCurrentHolder()) {
            divideExecution(commands)
        } else {
            buffer.addAll(commands)
            activityNavigationProvider.setOnHolderActiveListenerSingle { utilizeBuffer() }
        }
    }

    /**
     * Smart execution of a command queue.
     *
     * This method divides commands into sync and async, and executing them in blocking manner:
     *
     * 1. If our list starts with async commands, we execute them,
     * and postpone all remaining command execution
     * to wait until the new navigation holder becomes active (leading to step 2).
     *
     * 2. If our list starts with sync commands, we will execute them,
     * and then call this method again with remaining commands (returning to step 1)
     *
     * TODO: Dispatch commands in batches, to, for example,
     * TODO: execute multiple commands in one FragmentTransaction,
     * TODO: or use Context.startActivities(Intent, Intent...) to launch multiple activities.
     *
     */
    protected open fun divideExecution(commands: List<NavigationCommand>) {
        if (commands.isEmpty()) return

        val isStartingWithAsyncCommand = checkCommandAsync(commands.first())
        val asyncCommands: List<NavigationCommand>
        val syncCommands: List<NavigationCommand>

        if (isStartingWithAsyncCommand) {
            asyncCommands = commands.takeWhile(::checkCommandAsync)
            syncCommands = commands.takeLast(commands.size - asyncCommands.size)

            val hasAsyncCommands = asyncCommands.isNotEmpty()
            val hasSyncCommands = syncCommands.isNotEmpty()

            if (hasAsyncCommands) queueCommands(asyncCommands)

            when {
                hasAsyncCommands && hasSyncCommands -> postponeExecution(syncCommands)
                hasSyncCommands -> divideExecution(syncCommands)
            }
        } else {
            syncCommands = commands.takeWhile { !checkCommandAsync(it) }
            asyncCommands = commands.takeLast(commands.size - syncCommands.size)

            val hasAsyncCommands = asyncCommands.isNotEmpty()
            val hasSyncCommands = syncCommands.isNotEmpty()

            if (hasSyncCommands) queueCommands(syncCommands)
            if (hasAsyncCommands) safeExecuteWithBuffer(asyncCommands)
        }
    }

    /**
     * Puts navigation commands in queue if current [commandQueue] is not empty,
     * i.e if we're currently in process of executing commands.
     *
     * If the queue is empty, then we'll add new commands in queue,
     * consistently execute and remove commands.
     * Then we will check, if any pending commands are exists in queue.
     * If there are any pending commands in queue,
     * we remove them from queue and call this method again.
     */
    protected open fun queueCommands(commands: List<NavigationCommand>) {
        if (commandQueue.isNotEmpty()) {
            commandQueue.addAll(commands)
        } else {
            commandQueue.addAll(commands)

            commands.forEach { command ->
                dispatchCommand(command)
                commandQueue.remove(command)
            }

            if (commandQueue.isNotEmpty()) {
                val pendingCommands = LinkedList(commandQueue)
                commandQueue.clear()
                queueCommands(pendingCommands)
            }
        }
    }

    /**
     * Dispatches command to the right [NavigationCommandExecutor]
     */
    protected open fun dispatchCommand(command: NavigationCommand) {
        when (command) {
            is ActivityNavigationCommand -> activityCommandExecutor.execute(command)
            is FragmentNavigationCommand -> fragmentCommandExecutor.execute(command)
            is DialogNavigationCommand -> dialogCommandExecutor.execute(command)
        }
    }

    /**
     * Postpones command execution to the end of the Message Queue.
     */
    protected open fun postponeExecution(commands: List<NavigationCommand>) {
        Handler().post { safeExecuteWithBuffer(commands) }
    }

    /**
     * Checks whether the effect of this command will be shown immediately after execution,
     * or it will take some time to appear.
     */
    protected open fun checkCommandAsync(command: NavigationCommand): Boolean =
            command is ActivityNavigationCommand

    /**
     * Executes everything in buffer and cleans it.
     */
    protected fun utilizeBuffer() {
        execute(buffer)
        buffer.clear()
    }
}