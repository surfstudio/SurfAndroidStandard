package ru.surfstudio.navigation.command

import ru.surfstudio.navigation.animation.BaseScreenAnimations
import ru.surfstudio.navigation.animation.EmptyScreenAnimations
import ru.surfstudio.navigation.route.Route

/**
 * Замена текущего экрана на [route]
 */
data class Replace(
        override val route: Route,
        override val animations: BaseScreenAnimations = EmptyScreenAnimations
) : NavigationCommand
