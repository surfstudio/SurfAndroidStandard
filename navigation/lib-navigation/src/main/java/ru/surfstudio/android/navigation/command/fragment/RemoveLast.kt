package ru.surfstudio.android.navigation.command.fragment

import ru.surfstudio.android.navigation.animation.Animations
import ru.surfstudio.android.navigation.animation.resource.EmptyResourceAnimations
import ru.surfstudio.android.navigation.command.fragment.base.FragmentNavigationCommand
import ru.surfstudio.android.navigation.route.fragment.FragmentRoute
import ru.surfstudio.android.navigation.route.stub.StubFragmentRoute
import ru.surfstudio.android.navigation.route.stub.StubTabFragmentRoute

/**
 * Remove last screen from current stack and switch to previous.
 *
 * @param animations animations [Animations] used to specify navigation transition appearance
 * @param sourceTag tag of a source screen, which will execute navigation command.
 * @param isTab - are we removing fragment from tab navigator, or from default navigator
 */
data class RemoveLast(
        override val animations: Animations = EmptyResourceAnimations,
        override val sourceTag: String = "",
        val isTab: Boolean = false
) : FragmentNavigationCommand {

    override val route: FragmentRoute = if (isTab) StubTabFragmentRoute else StubFragmentRoute
}