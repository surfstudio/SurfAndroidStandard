package ru.surfstudio.android.navigation.executor

import android.os.Handler
import android.os.Looper
import ru.surfstudio.android.navigation.command.NavigationCommand
import ru.surfstudio.android.navigation.command.activity.Finish
import ru.surfstudio.android.navigation.command.activity.FinishAffinity
import ru.surfstudio.android.navigation.command.activity.Start
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

    protected val handler = Handler(Looper.getMainLooper())
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
            splitExecutionIntoChunks(commands)
        } else {
            postponeExecution(commands)
        }
    }


    /**
     * Postpones command execution to the end of the Message Queue
     * and executes [commands] with the next available navigator
     */
    protected fun postponeExecution(commands: List<NavigationCommand>) {
        handler.post {
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
     * TODO: execute multiple commands in one FragmentTransaction
     *
     */
    protected open fun splitExecutionIntoChunks(commands: List<NavigationCommand>) {
        if (commands.isEmpty()) return

        val firstCommand = commands.first()
        val isStartingWithAsyncCommand = checkCommandAsync(firstCommand)

        if (isStartingWithAsyncCommand) {
            splitStartingFromAsync(commands, firstCommand)
        } else {
            splitStartingFromSync(commands)
        }
    }

    private fun splitStartingFromSync(commands: List<NavigationCommand>) {
        val firstAsyncCommandIndex = commands.indexOfFirst { checkCommandAsync(it) }
        if (firstAsyncCommandIndex >= 1) {
            queueCommands(commands.take(firstAsyncCommandIndex))
            safeExecuteWithBuffer(commands.subList(firstAsyncCommandIndex, commands.size))
        } else {
            queueCommands(commands)
        }
    }

    private fun splitStartingFromAsync(commands: List<NavigationCommand>, firstCommand: NavigationCommand) {
        val firstNotStartIndex = commands.indexOfFirst { command -> command !is Start }
        when {
            /** We have only Start commands, just execute them */
            firstNotStartIndex == -1 -> {
                startSeveralActivities(commands)
            }
            /**
             * We have several Start commands and commands of other types right after them, so
             * we start activities first and postpone the execution of the remaining commands until
             * new navigation holder become available
             */
            firstNotStartIndex > 1 -> {
                startSeveralActivities(commands.take(firstNotStartIndex))
                postponeExecution(commands.subList(firstNotStartIndex, commands.size))
            }
            /**
             * Here are other cases, where we just execute first command.
             * If we have to Finish activity or affinity we need to check the next command after
             * it. If it's async but not Finish/FinishAffinity command we can immediately execute it
             * with current navigation holder. Otherwise we postpone execution until new navigation
             * holder becomes available.
             */
            else -> {
                dispatchCommand(firstCommand)
                val restCommands = commands.drop(1)
                val canExecuteImmediately: Boolean = restCommands.isNotEmpty()
                        && checkCommandAsync(restCommands[0])
                        && !isFinishCommand(restCommands[0])
                if (canExecuteImmediately) {
                    splitExecutionIntoChunks(restCommands)
                } else {
                    postponeExecution(restCommands)
                }
            }
        }
    }

    private fun isFinishCommand(command: NavigationCommand): Boolean {
        return command is Finish || command is FinishAffinity
    }

    private fun startSeveralActivities(commands: List<NavigationCommand>) {
        activityCommandExecutor.execute(commands.map { it as ActivityNavigationCommand })
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
     * Checks whether the effect of this command will be shown immediately after execution,
     * or it will take some time to appear.
     */
    protected open fun checkCommandAsync(command: NavigationCommand): Boolean =
            command is ActivityNavigationCommand

    /**
     * Executes everything in buffer and cleans it.
     */
    protected fun utilizeBuffer() {
        val commands = ArrayList(buffer)
        buffer.clear()
        handler.post {
            execute(commands)
        }
    }
}