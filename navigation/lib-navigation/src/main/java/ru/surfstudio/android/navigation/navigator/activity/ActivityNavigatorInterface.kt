package ru.surfstudio.android.navigation.navigator.activity

import android.os.Bundle
import ru.surfstudio.android.navigation.animation.Animations
import ru.surfstudio.android.navigation.route.activity.ActivityRoute

/**
 *  Base interface for an activity navigator.
 */
interface ActivityNavigatorInterface {

    fun start(routes: List<ActivityRoute>, animations: Animations, activityOptions: Bundle?)

    fun start(route: ActivityRoute, animations: Animations, activityOptions: Bundle?)

    fun replace(route: ActivityRoute, animations: Animations, activityOptions: Bundle?)

    fun finish()

    fun finishAffinity()

    fun canBeStarted(route: ActivityRoute): Boolean
}