package ru.surfstudio.navigation.command

import ru.surfstudio.navigation.animation.BaseScreenAnimations
import ru.surfstudio.navigation.animation.EmptyScreenAnimations
import ru.surfstudio.navigation.route.Route

/**
 * Команда добавления экрана
 */
data class Add(
        override val route: Route,
        override val animations: BaseScreenAnimations = EmptyScreenAnimations
) : NavigationCommand
