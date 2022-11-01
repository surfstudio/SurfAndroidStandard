package ru.surfstudio.android.navigation.command.activity

import android.os.Bundle
import ru.surfstudio.android.navigation.animation.Animations
import ru.surfstudio.android.navigation.animation.DefaultAnimations
import ru.surfstudio.android.navigation.command.activity.base.ActivityNavigationCommand
import ru.surfstudio.android.navigation.route.activity.ActivityRoute

/**
 * Starts new activity screen
 */
data class Start(
        override val route: ActivityRoute,
        override val animations: Animations = DefaultAnimations.activity,
        val activityOptions: Bundle? = null
) : ActivityNavigationCommand