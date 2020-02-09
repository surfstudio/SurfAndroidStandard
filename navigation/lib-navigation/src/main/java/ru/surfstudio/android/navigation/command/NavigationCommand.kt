package ru.surfstudio.android.navigation.command

import ru.surfstudio.android.navigation.animation.Animations
import ru.surfstudio.android.navigation.option.Options
import ru.surfstudio.android.navigation.route.Route
import java.io.Serializable

/**
 * #Base navigation command.
 *
 * Used in screen navigation process to execute specific action with a screen.
 * Consists of three main parameters parameters.
 * ### Mandatory
 * * route [Route] that identifies the screen and makes command
 * ### Optional
 * * animations [Animations] used to specify navigation transition appearance.
 * * options [Options] additional options used to tweak navigation transition behavior.
 */
interface NavigationCommand : Serializable {

    val route: Route

    val animations: Animations?

    val options: Options?
}
