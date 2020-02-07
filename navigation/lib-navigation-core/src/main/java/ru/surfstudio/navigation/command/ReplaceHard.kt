package ru.surfstudio.navigation.command

import ru.surfstudio.navigation.animation.Animations
import ru.surfstudio.navigation.animation.EmptyScreenAnimations
import ru.surfstudio.navigation.route.Route

/**
 * Replace fragment without putting previous fragment in stack
 * This command is equal to calling removeLast and add at the same time.
 */
data class ReplaceHard(
        override val route: Route,
        override val animations: Animations = EmptyScreenAnimations
) : NavigationCommand
