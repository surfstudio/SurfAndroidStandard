package ru.surfstudio.android.navigation.command

import ru.surfstudio.android.navigation.animation.Animations
import ru.surfstudio.android.navigation.animation.EmptyScreenAnimations
import ru.surfstudio.android.navigation.option.Options
import ru.surfstudio.android.navigation.route.Route

/**
 * Replace screen without putting previous screen in stack.
 *
 * This command is equal to calling removeLast and add at the same time.
 *
 *  This operation supports back stack.
 */
data class ReplaceHard(
        override val route: Route,
        override val animations: Animations = EmptyScreenAnimations,
        override val options: Options? = null
) : NavigationCommand
