package ru.surfstudio.android.navigation.observer.command

import android.os.Bundle
import ru.surfstudio.android.navigation.animation.Animations
import ru.surfstudio.android.navigation.animation.DefaultAnimations
import ru.surfstudio.android.navigation.command.activity.base.ActivityNavigationCommand
import ru.surfstudio.android.navigation.observer.route.ActivityWithResultRoute
import java.io.Serializable

class StartForResult<T : Serializable, R>(
        override val route: R,
        override val animations: Animations = DefaultAnimations.activity,
        val activityOptions: Bundle? = null
) : ActivityNavigationCommand where R : ActivityWithResultRoute<T>
