package ru.surfstudio.android.navigation.command.fragment

import ru.surfstudio.android.navigation.animation.Animations
import ru.surfstudio.android.navigation.animation.resource.EmptyResourceAnimations
import ru.surfstudio.android.navigation.command.fragment.base.FragmentNavigationCommand
import ru.surfstudio.android.navigation.route.fragment.FragmentRoute
import ru.surfstudio.android.navigation.route.stub.StubFragmentRoute


/**
 * Команда удаления всех экранов из текущего стека
 */
data class RemoveAll(
        override val animations: Animations = EmptyResourceAnimations
) : FragmentNavigationCommand {

    override val route: FragmentRoute = StubFragmentRoute
}