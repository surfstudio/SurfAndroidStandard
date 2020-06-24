package ru.surfstudio.android.navigation.executor.screen.fragment

import ru.surfstudio.android.navigation.command.activity.base.ActivityNavigationCommand
import ru.surfstudio.android.navigation.command.fragment.*
import ru.surfstudio.android.navigation.command.fragment.base.FragmentNavigationCommand
import ru.surfstudio.android.navigation.provider.ActivityNavigationProvider
import ru.surfstudio.android.navigation.executor.CommandExecutor
import ru.surfstudio.android.navigation.navigator.fragment.FragmentNavigatorInterface
import ru.surfstudio.android.navigation.route.tab.TabRoute

/**
 * Command executor for [ActivityNavigationCommand]s.
 *
 * There should be single instance of this executor for an application.
 */
open class FragmentCommandExecutor(
        private val activityNavigationProvider: ActivityNavigationProvider
) : CommandExecutor<FragmentNavigationCommand> {

    override fun execute(command: FragmentNavigationCommand) {
        val activityHolder = activityNavigationProvider.provide()
        val fragmentNavigationHolder = activityHolder.fragmentNavigationProvider.provide(command.sourceTag)
        val tabFragmentNavigator = fragmentNavigationHolder.tabFragmentNavigator
        val fragmentNavigator = fragmentNavigationHolder.fragmentNavigator
        when (command.route) {
            is TabRoute -> execute(command, tabFragmentNavigator)
            else -> execute(command, fragmentNavigator)
        }
    }

    protected open fun execute(command: FragmentNavigationCommand, navigator: FragmentNavigatorInterface) {
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

    override fun execute(commands: List<FragmentNavigationCommand>) {
        TODO("Fragment navigation command list execution")
    }
}