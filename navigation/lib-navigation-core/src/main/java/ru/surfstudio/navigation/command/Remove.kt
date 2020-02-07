package ru.surfstudio.navigation.command

import ru.surfstudio.navigation.animation.Animations
import ru.surfstudio.navigation.animation.EmptyScreenAnimations
import ru.surfstudio.navigation.route.Route

/**
 * Команда удаление экрана
 */
data class Remove(
        override val route: Route,
        override val animations: Animations = EmptyScreenAnimations
) : NavigationCommand
