package ru.surfstudio.android.navigation.command.fragment

import ru.surfstudio.android.navigation.animation.Animations
import ru.surfstudio.android.navigation.animation.resource.NoResourceAnimations
import ru.surfstudio.android.navigation.command.fragment.base.FragmentNavigationCommand
import ru.surfstudio.android.navigation.route.fragment.FragmentRoute
import ru.surfstudio.android.navigation.route.stub.StubFragmentRoute

/**
 * Remove last screen from current stack and switch to previous.
 *
 * @param animations animations [Animations] used to specify navigation transition appearance
 * @param sourceTag tag of a source screen, which will execute navigation command.
 */
data class RemoveLast(
        override val animations: Animations = NoResourceAnimations,
        override var sourceTag: String = ""
) : FragmentNavigationCommand {

    override val route: FragmentRoute = StubFragmentRoute
}