package ru.surfstudio.android.navigation.navigator.activity

import android.os.Bundle
import ru.surfstudio.android.navigation.animation.Animations
import ru.surfstudio.android.navigation.route.activity.ActivityRoute

/**
 *  Base interface for an activity navigator.

 */
interface ActivityNavigatorInterface {

    fun start(route: ActivityRoute, animations: Animations, optionsCompat: Bundle?)

    fun replace(route: ActivityRoute, animations: Animations, optionsCompat: Bundle?)

    fun finish()

    fun finishAffinity()
}