package ru.surfstudio.navigation.command

import ru.surfstudio.navigation.animation.BaseScreenAnimations
import ru.surfstudio.navigation.animation.EmptyScreenAnimations
import ru.surfstudio.navigation.route.Route

/**
 * Удаление экранов вплоть до [route].
 */
data class RemoveUntil(
        override val route: Route,
        override val animations: BaseScreenAnimations = EmptyScreenAnimations
) : NavigationCommand
