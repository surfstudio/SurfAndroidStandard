package ru.surfstudio.android.navigation.observer.command.activity

import ru.surfstudio.android.navigation.animation.Animations
import ru.surfstudio.android.navigation.command.activity.base.ActivityNavigationCommand
import ru.surfstudio.android.navigation.observer.route.PermissionRequestRoute

/**
 * Command for requesting permission.
 * This is used in [PermissionManager], prefer use it.
 */
class RequestPermission(
    override val route: PermissionRequestRoute,
    override val animations: Animations? = null
) : ActivityNavigationCommand
