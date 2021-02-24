package ru.surfstudio.android.navigation.command.activity.base

import android.os.Bundle
import ru.surfstudio.android.navigation.animation.Animations
import ru.surfstudio.android.navigation.command.NavigationCommand
import ru.surfstudio.android.navigation.route.activity.ActivityRoute

/**
 * Command that is used in navigation process with [androidx.appcompat.app.AppCompatActivity].
 */
interface ActivityNavigationCommand: NavigationCommand {
    override val route: ActivityRoute
    override val animations: Animations
    val activityOptions: Bundle?
}