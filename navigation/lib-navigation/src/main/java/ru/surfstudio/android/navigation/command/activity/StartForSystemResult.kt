package ru.surfstudio.android.navigation.command.activity

import android.os.Bundle
import ru.surfstudio.android.navigation.animation.Animations
import ru.surfstudio.android.navigation.animation.DefaultAnimations
import ru.surfstudio.android.navigation.command.activity.base.ActivityNavigationCommand
import ru.surfstudio.android.navigation.route.result.SystemActivityResultRoute

/**
 * Starts new system activity screen for result
 */
data class StartForSystemResult(
        override val route: SystemActivityResultRoute<*>,
        override val animations: Animations = DefaultAnimations.activity,
        val activityOptions: Bundle? = null
) : ActivityNavigationCommand