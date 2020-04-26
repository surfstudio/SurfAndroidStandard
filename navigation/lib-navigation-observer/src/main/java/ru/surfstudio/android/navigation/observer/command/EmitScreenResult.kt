package ru.surfstudio.android.navigation.observer.command

import ru.surfstudio.android.navigation.animation.Animations
import ru.surfstudio.android.navigation.command.NavigationCommand
import ru.surfstudio.android.navigation.route.BaseRoute
import java.io.Serializable

/**
 * Command that emits screen result.
 * @param sourceRoute [BaseRoute] of a screen, that is observing result
 * @param route target [BaseRoute] of a screen, that is emitting result
 * @param result result from the target screen.
 *
 * This command can be handled in [ru.surfstudio.android.navigation.observer.executor.AppCommandExecutorWithResult].
 */
class EmitScreenResult<T : Serializable>(
        val sourceRoute: BaseRoute<*>,
        override val route: BaseRoute<*>,
        val result: T
) : NavigationCommand {
    override val animations: Animations? = null
}