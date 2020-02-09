package ru.surfstudio.android.navigation.command

import ru.surfstudio.android.navigation.animation.Animations
import ru.surfstudio.android.navigation.animation.EmptyScreenAnimations
import ru.surfstudio.android.navigation.option.Options
import ru.surfstudio.android.navigation.option.activity.ActivityBundleOptions
import ru.surfstudio.android.navigation.route.Route

/**
 * Add new screen to a screen stack.
 *
 * Can be supplied with [ActivityBundleOptions] to specify ActivityOptions for starting new screen.
 *
 * This operation supports back stack.
 */
data class Add(
        override val route: Route,
        override val animations: Animations = EmptyScreenAnimations,
        override val options: Options? = null
) : NavigationCommand
