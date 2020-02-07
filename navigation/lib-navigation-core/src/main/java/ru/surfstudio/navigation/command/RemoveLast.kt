package ru.surfstudio.navigation.command

import ru.surfstudio.navigation.animation.BaseScreenAnimations
import ru.surfstudio.navigation.animation.NoScreenAnimations
import ru.surfstudio.navigation.route.Route

/**
 * Удаление последнего экрана и переключение на предыдущий
 */
open class RemoveLast(
        override val route: Route,
        override val animations: BaseScreenAnimations = NoScreenAnimations
) : NavigationCommand

/**
 * Команда удаления последней Activity из текущего стека
 */
data class RemoveLastActivity(
    override val animations: BaseScreenAnimations = NoScreenAnimations
) : RemoveLast(TODO(), animations)

/**
 * Команда удаления последнего фрагмента из текущего стека
 */
data class RemoveLastFragment(
    override val animations: BaseScreenAnimations = NoScreenAnimations
) : RemoveLast(TODO(), animations)
