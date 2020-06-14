package ru.surfstudio.android.navigation.command.fragment

import ru.surfstudio.android.navigation.animation.Animations
import ru.surfstudio.android.navigation.animation.DefaultAnimations
import ru.surfstudio.android.navigation.command.fragment.base.FragmentNavigationCommand
import ru.surfstudio.android.navigation.route.fragment.FragmentRoute
import ru.surfstudio.android.navigation.route.stub.StubFragmentRoute
import ru.surfstudio.android.navigation.route.stub.StubTabFragmentRoute


/**
 * Command to remove all fragments from stack.
 *
 * @param animations animations [Animations] used to specify navigation transition appearance
 * @param sourceTag tag of a source screen, which will execute navigation command.
 * @param isTab are we removing fragment from tab navigator, or from default navigator
 */
data class RemoveAll(
        override val animations: Animations = DefaultAnimations.fragment,
        override var sourceTag: String = "",
        val isTab: Boolean = false
) : FragmentNavigationCommand {

    override val route: FragmentRoute = if (isTab) StubTabFragmentRoute else StubFragmentRoute
}