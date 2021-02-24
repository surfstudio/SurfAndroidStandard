package ru.surfstudio.android.navigation.observer.command.activity

import android.os.Bundle
import ru.surfstudio.android.navigation.animation.Animations
import ru.surfstudio.android.navigation.animation.DefaultAnimations
import ru.surfstudio.android.navigation.command.activity.base.ActivityNavigationCommand
import ru.surfstudio.android.navigation.observer.route.ActivityWithResultRoute
import java.io.Serializable

/**
 * Navigation command to start screen for result.
 * Used to start system screens or screens which doesn't support EmitScreenResult commands.
 */
class StartForResult<T : Serializable, R>(
        override val route: R,
        override val animations: Animations = DefaultAnimations.activity,
        override val activityOptions: Bundle? = null
) : ActivityNavigationCommand where R : ActivityWithResultRoute<T>
