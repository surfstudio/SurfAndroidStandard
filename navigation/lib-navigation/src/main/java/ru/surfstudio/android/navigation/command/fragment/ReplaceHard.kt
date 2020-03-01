package ru.surfstudio.android.navigation.command.fragment

import ru.surfstudio.android.navigation.animation.Animations
import ru.surfstudio.android.navigation.animation.EmptyScreenAnimations
import ru.surfstudio.android.navigation.command.fragment.base.FragmentNavigationCommand
import ru.surfstudio.android.navigation.route.fragment.FragmentRoute

/**
 * Replace screen without putting previous screen in stack.
 *
 * This command is equal to calling removeLast and add at the same time.
 *
 *  This operation supports back stack.
 */
data class ReplaceHard(
        override val route: FragmentRoute,
        override val animations: Animations = EmptyScreenAnimations
) : FragmentNavigationCommand
