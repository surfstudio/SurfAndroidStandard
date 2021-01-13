package ru.surfstudio.android.navigation.navigator.activity

import android.os.Bundle
import ru.surfstudio.android.navigation.animation.Animations
import ru.surfstudio.android.navigation.route.activity.ActivityRoute
import ru.surfstudio.android.navigation.route.result.ActivityResultRoute

/**
 *  Base interface for an activity navigator.
 */
interface ActivityNavigatorInterface {

    fun start(route: ActivityRoute, animations: Animations, activityOptions: Bundle?)

    fun startForResult(route: ActivityResultRoute<*>, animations: Animations, activityOptions: Bundle?)

    fun replace(route: ActivityRoute, animations: Animations, activityOptions: Bundle?)

    fun finish()

    fun finishAffinity()

    fun canBeStarted(route: ActivityRoute): Boolean
}