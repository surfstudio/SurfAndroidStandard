package ru.surfstudio.android.navigation.command.fragment

import ru.surfstudio.android.navigation.animation.Animations
import ru.surfstudio.android.navigation.animation.DefaultAnimations
import ru.surfstudio.android.navigation.command.fragment.base.FragmentNavigationCommand
import ru.surfstudio.android.navigation.route.fragment.FragmentRoute
import ru.surfstudio.android.navigation.route.stub.StubFragmentRoute


/**
 * Command to remove all fragments from stack.
 *
 * @param animations animations [Animations] used to specify navigation transition appearance
 * @param sourceTag tag of a source screen, which will execute navigation command.
 * @param shouldRemoveLast flag which determines whether we should wipe all fragments in stack, or
 * leave last one alive.
 */
data class RemoveAll(
        override val animations: Animations = DefaultAnimations.fragment,
        override var sourceTag: String = "",
        val shouldRemoveLast: Boolean = false
) : FragmentNavigationCommand {

    override val route: FragmentRoute = StubFragmentRoute
}