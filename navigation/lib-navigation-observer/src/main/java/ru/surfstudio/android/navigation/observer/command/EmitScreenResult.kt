package ru.surfstudio.android.navigation.observer.command

import ru.surfstudio.android.navigation.animation.Animations
import ru.surfstudio.android.navigation.command.NavigationCommand
import ru.surfstudio.android.navigation.observer.route.ResultRoute
import ru.surfstudio.android.navigation.route.BaseRoute
import java.io.Serializable

/**
 * Command that emits screen result.
 * @param route target [BaseRoute] of a screen, that is emitting result
 * @param result result from the target screen.
 *
 * This command can be handled in [ru.surfstudio.android.navigation.observer.executor.AppCommandExecutorWithResult].
 */
class EmitScreenResult<T : Serializable, R : ResultRoute<T>>(
    override val route: R,
    val result: T
) : NavigationCommand {
    override val animations: Animations? = null
}