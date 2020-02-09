package ru.surfstudio.android.navigation.command

import ru.surfstudio.android.navigation.animation.Animations
import ru.surfstudio.android.navigation.animation.EmptyScreenAnimations
import ru.surfstudio.android.navigation.option.Options
import ru.surfstudio.android.navigation.route.Route

/**
 * Remove last screen without adding previous to a stack.
 */
data class Remove(
        override val route: Route,
        override val animations: Animations = EmptyScreenAnimations,
        override val options: Options? = null
) : NavigationCommand
