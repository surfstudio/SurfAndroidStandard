package ru.surfstudio.android.navigation.command.fragment

import ru.surfstudio.android.navigation.animation.Animations
import ru.surfstudio.android.navigation.animation.resource.NoResourceAnimations
import ru.surfstudio.android.navigation.command.fragment.base.FragmentNavigationCommand
import ru.surfstudio.android.navigation.route.fragment.FragmentRoute

/**
 * Remove all screens above [route] in the stack.
 *
 * @param animations animations [Animations] used to specify navigation transition appearance
 * @param sourceTag tag of a source screen, which will execute navigation command.
 * @param isInclusive flag which determines if the [route] should be removed along with the screens above it.
 */
data class RemoveUntil(
        override val route: FragmentRoute,
        override val animations: Animations = NoResourceAnimations,
        override var sourceTag: String = "",
        val isInclusive: Boolean
) : FragmentNavigationCommand
