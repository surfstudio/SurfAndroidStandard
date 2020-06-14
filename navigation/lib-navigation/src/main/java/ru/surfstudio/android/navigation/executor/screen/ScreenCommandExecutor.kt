package ru.surfstudio.android.navigation.executor.screen

import ru.surfstudio.android.navigation.command.NavigationCommand
import ru.surfstudio.android.navigation.command.fragment.base.FragmentNavigationCommand
import ru.surfstudio.android.navigation.executor.CommandExecutor
import ru.surfstudio.android.navigation.executor.NavigationCommandExecutor
import ru.surfstudio.android.navigation.provider.FragmentProvider

/**
 * [CommandExecutor] for [NavigationCommand], that uses standard standard CommandExecutor,
 * and operates in the screen scope, which gives it ability to extract some parameters,
 * bound to current screen scope.
 *
 * For example, this particular executor automatically supplies all
 * [FragmentNavigationCommand] without sourceTag,  with tag, extracted from a current fragment.
 */
//@PerScreen
open class ScreenCommandExecutor(
        private val fragmentProvider: FragmentProvider,
        private val appExecutor: CommandExecutor<NavigationCommand>
) : NavigationCommandExecutor {

    override fun execute(command: NavigationCommand) {
        supplyCommandWithCurrentFragmentTag(command)
        appExecutor.execute(command)
    }

    override fun execute(commands: List<NavigationCommand>) {
        commands.forEach(::supplyCommandWithCurrentFragmentTag)
        appExecutor.execute(commands)
    }

    /**
     * Supplies [FragmentNavigationCommand] with empty[FragmentNavigationCommand.sourceTag] with
     * new sourceTag, extracted from current scope fragment's tag.
     */
    private fun supplyCommandWithCurrentFragmentTag(command: NavigationCommand) {
        if (command !is FragmentNavigationCommand) return
        if (command.sourceTag.isNotEmpty()) return
        val currentTag = fragmentProvider.provide()?.tag ?: return
        command.sourceTag = currentTag
    }
}