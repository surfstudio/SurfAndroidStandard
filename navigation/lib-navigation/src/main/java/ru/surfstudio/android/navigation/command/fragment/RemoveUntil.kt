package ru.surfstudio.android.navigation.command.fragment

import ru.surfstudio.android.navigation.animation.Animations
import ru.surfstudio.android.navigation.animation.EmptyScreenAnimations
import ru.surfstudio.android.navigation.command.fragment.base.FragmentNavigationCommand
import ru.surfstudio.android.navigation.option.fragment.RemoveInclusive
import ru.surfstudio.android.navigation.route.fragment.FragmentRoute

/**
 * Remove all screens above [route] in the stack.
 *
 * Can be supplied with [RemoveInclusive] option to remove the [route] along with a screens above.
 */
data class RemoveUntil(
        override val route: FragmentRoute,
        override val animations: Animations = EmptyScreenAnimations,
        val isInclusive: Boolean
) : FragmentNavigationCommand
