package ru.surfstudio.android.navigation.command.fragment

import ru.surfstudio.android.navigation.animation.Animations
import ru.surfstudio.android.navigation.animation.EmptyScreenAnimations
import ru.surfstudio.android.navigation.command.fragment.base.FragmentNavigationCommand
import ru.surfstudio.android.navigation.route.fragment.FragmentRoute
import ru.surfstudio.android.navigation.route.stub.StubFragmentRoute

/**
 * Remove last screen from current stack and switch to previous.
 */
data class RemoveLast(
        override val animations: Animations = EmptyScreenAnimations
) : FragmentNavigationCommand {

    override val route: FragmentRoute = StubFragmentRoute
}