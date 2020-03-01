package ru.surfstudio.android.navigation.executor.fragment

import ru.surfstudio.android.navigation.command.fragment.*
import ru.surfstudio.android.navigation.command.fragment.base.FragmentNavigationCommand
import ru.surfstudio.android.navigation.executor.CommandExecutor
import ru.surfstudio.android.navigation.navigator.fragment.FragmentNavigator
import ru.surfstudio.android.navigation.navigator.fragment.FragmentNavigatorInterface
import ru.surfstudio.android.navigation.navigator.fragment.tab.TabFragmentNavigator

open class FragmentCommandExecutor(
        private val fragmentNavigator: FragmentNavigator,
        private val tabFragmentNavigator: TabFragmentNavigator
): CommandExecutor<FragmentNavigationCommand> {

    open override fun execute(command: FragmentNavigationCommand) {
        when (command.route) { }
    }

    fun execute(command: FragmentNavigationCommand, navigator: FragmentNavigatorInterface) {
        when (command) {
            is Add -> navigator.add(command.route, command.animations)
            is Replace -> navigator.replace(command.route, command.animations)
            is ReplaceHard -> navigator.replaceHard(command.route, command.animations)
            is Remove -> navigator.remove(command.route, command.animations)
            is RemoveLast -> navigator.removeLast(command.animations)
            is RemoveUntil -> navigator.removeUntil(command.route, command.isInclusive)
            is RemoveAll -> navigator.removeAll()
        }
    }
}