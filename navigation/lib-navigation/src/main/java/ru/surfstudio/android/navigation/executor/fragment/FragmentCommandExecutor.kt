package ru.surfstudio.android.navigation.executor.fragment

import ru.surfstudio.android.navigation.command.fragment.*
import ru.surfstudio.android.navigation.command.fragment.base.FragmentNavigationCommand
import ru.surfstudio.android.navigation.executor.CommandExecutor
import ru.surfstudio.android.navigation.navigator.fragment.FragmentNavigator

open class FragmentCommandExecutor(
        private val fragmentNavigator: FragmentNavigator
): CommandExecutor<FragmentNavigationCommand> {

    open override fun execute(command: FragmentNavigationCommand) {
        when (command) {
            is Add -> fragmentNavigator.add(command.route, command.animations)
            is Replace -> fragmentNavigator.replace(command.route, command.animations)
            is ReplaceHard -> fragmentNavigator.replaceHard(command.route, command.animations)
            is Remove -> fragmentNavigator.remove(command.route, command.animations)
            is RemoveLast -> fragmentNavigator.removeLast(command.animations)
            is RemoveUntil -> fragmentNavigator.removeUntil(command.route, command.isInclusive)
            is RemoveAll -> fragmentNavigator.removeAll()
        }
    }
}