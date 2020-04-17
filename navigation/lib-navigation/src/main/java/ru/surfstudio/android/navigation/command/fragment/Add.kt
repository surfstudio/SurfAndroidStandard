package ru.surfstudio.android.navigation.command.fragment

import ru.surfstudio.android.navigation.animation.Animations
import ru.surfstudio.android.navigation.animation.DefaultAnimations
import ru.surfstudio.android.navigation.command.fragment.base.FragmentNavigationCommand
import ru.surfstudio.android.navigation.route.fragment.FragmentRoute

/**
 * Add new screen to a screen stack.
 *
 * This operation supports back stack.
 */
data class Add(
        override val route: FragmentRoute,
        override val animations: Animations = DefaultAnimations.fragment,
        override val sourceTag: String = ""
) : FragmentNavigationCommand
