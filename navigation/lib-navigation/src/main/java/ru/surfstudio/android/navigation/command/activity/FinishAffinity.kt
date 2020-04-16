package ru.surfstudio.android.navigation.command.activity

import ru.surfstudio.android.navigation.animation.Animations
import ru.surfstudio.android.navigation.animation.resource.EmptyResourceAnimations
import ru.surfstudio.android.navigation.command.activity.base.ActivityNavigationCommand
import ru.surfstudio.android.navigation.route.activity.ActivityRoute
import ru.surfstudio.android.navigation.route.stub.StubActivityRoute

/**
 * Finish current activity task
 */
data class FinishAffinity(
        override val animations: Animations = EmptyResourceAnimations
) : ActivityNavigationCommand {

    override val route: ActivityRoute = StubActivityRoute
}