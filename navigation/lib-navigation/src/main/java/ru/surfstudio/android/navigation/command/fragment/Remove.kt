package ru.surfstudio.android.navigation.command.fragment

import ru.surfstudio.android.navigation.animation.Animations
import ru.surfstudio.android.navigation.animation.resource.EmptyResourceAnimations
import ru.surfstudio.android.navigation.command.fragment.base.FragmentNavigationCommand
import ru.surfstudio.android.navigation.route.fragment.FragmentRoute

/**
 * Remove last screen without adding previous to a stack.
 */
data class Remove(
        override val route: FragmentRoute,
        override val animations: Animations = EmptyResourceAnimations,
        override val sourceTag: String = ""
) : FragmentNavigationCommand
