package ru.surfstudio.android.navigation.command.activity

import ru.surfstudio.android.navigation.animation.Animations
import ru.surfstudio.android.navigation.animation.res.EmptyResourceAnimations
import ru.surfstudio.android.navigation.command.activity.base.ActivityNavigationCommand
import ru.surfstudio.android.navigation.route.activity.ActivityRoute

/**
 * Finish current activity task
 */
data class FinishAffinity(
        override val route: ActivityRoute,
        override val animations: Animations = EmptyResourceAnimations
) : ActivityNavigationCommand