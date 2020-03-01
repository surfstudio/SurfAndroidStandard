package ru.surfstudio.android.navigation.command.activity

import android.os.Bundle
import ru.surfstudio.android.navigation.animation.Animations
import ru.surfstudio.android.navigation.animation.EmptyScreenAnimations
import ru.surfstudio.android.navigation.command.activity.base.ActivityNavigationCommand
import ru.surfstudio.android.navigation.route.activity.ActivityRoute

data class Start(
        override val route: ActivityRoute,
        override val animations: Animations = EmptyScreenAnimations,
        val options: Bundle
) : ActivityNavigationCommand