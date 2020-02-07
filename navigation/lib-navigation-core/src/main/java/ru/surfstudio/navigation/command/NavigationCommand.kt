package ru.surfstudio.navigation.command

import ru.surfstudio.navigation.animation.Animations
import ru.surfstudio.navigation.options.Options
import ru.surfstudio.navigation.route.Route
import java.io.Serializable

/**
 * Команда навигации.
 *
 * Используется в механизме навигации.
 *
 * Содержит данные для открытия экрана [route]
 */
interface NavigationCommand : Serializable {

    val route: Route

    val animations: Animations? get() = null

    val options: Options? get() = null
}
