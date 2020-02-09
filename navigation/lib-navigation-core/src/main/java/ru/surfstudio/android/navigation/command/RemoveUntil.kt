package ru.surfstudio.android.navigation.command

import ru.surfstudio.android.navigation.animation.Animations
import ru.surfstudio.android.navigation.animation.EmptyScreenAnimations
import ru.surfstudio.android.navigation.option.Options
import ru.surfstudio.android.navigation.route.Route
import ru.surfstudio.android.navigation.option.fragment.RemoveInclusive

/**
 * Remove all screens above [route] in the stack.
 *
 * Can be supplied with [RemoveInclusive] option to remove the [route] along with a screens above.
 */
data class RemoveUntil(
        override val route: Route,
        override val animations: Animations = EmptyScreenAnimations,
        override val options: Options? = null
) : NavigationCommand
