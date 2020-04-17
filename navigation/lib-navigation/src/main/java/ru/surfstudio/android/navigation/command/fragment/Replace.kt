package ru.surfstudio.android.navigation.command.fragment

import ru.surfstudio.android.navigation.animation.Animations
import ru.surfstudio.android.navigation.animation.DefaultAnimations
import ru.surfstudio.android.navigation.command.fragment.base.FragmentNavigationCommand
import ru.surfstudio.android.navigation.route.fragment.FragmentRoute

/**
 * Replace screen in a current stack with a screen specified by [route].
 *
 * Can be supplied with [ActivityBundleOptions] to specify ActivityOptions for starting new screen,
 *
 * This operation supports back stack.
 */
data class Replace(
        override val route: FragmentRoute,
        override val animations: Animations = DefaultAnimations.fragment,
        override val sourceTag: String = ""
) : FragmentNavigationCommand
