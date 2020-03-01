package ru.surfstudio.android.navigation.command.fragment

import ru.surfstudio.android.navigation.animation.Animations
import ru.surfstudio.android.navigation.animation.resource.EmptyResourceAnimations
import ru.surfstudio.android.navigation.command.fragment.base.FragmentNavigationCommand
import ru.surfstudio.android.navigation.route.fragment.FragmentRoute
import ru.surfstudio.android.navigation.route.stub.StubFragmentRoute

/**
 * Remove last screen from current stack and switch to previous.
 */
data class RemoveLast(
        override val animations: Animations = EmptyResourceAnimations
) : FragmentNavigationCommand {

    override val route: FragmentRoute = StubFragmentRoute
}