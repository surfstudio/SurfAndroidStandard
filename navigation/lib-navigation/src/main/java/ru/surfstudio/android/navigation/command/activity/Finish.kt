package ru.surfstudio.android.navigation.command.activity

import ru.surfstudio.android.navigation.animation.Animations
import ru.surfstudio.android.navigation.animation.resource.EmptyResourceAnimations
import ru.surfstudio.android.navigation.command.activity.base.ActivityNavigationCommand
import ru.surfstudio.android.navigation.route.activity.ActivityRoute

data class Finish(
        override val route: ActivityRoute,
        override val animations: Animations = EmptyResourceAnimations
) : ActivityNavigationCommand