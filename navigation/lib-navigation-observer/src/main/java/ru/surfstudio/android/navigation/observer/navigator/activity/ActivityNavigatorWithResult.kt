package ru.surfstudio.android.navigation.observer.navigator.activity

import android.os.Bundle
import ru.surfstudio.android.navigation.animation.Animations
import ru.surfstudio.android.navigation.navigator.activity.ActivityNavigatorInterface
import ru.surfstudio.android.navigation.observer.route.ActivityWithResultRoute
import ru.surfstudio.android.navigation.observer.route.PermissionRequestRoute

/**
 *  Interface for an activity navigator with method start for result.
 */
interface ActivityNavigatorWithResult : ActivityNavigatorInterface {

    /**
     * Starts a new activity for getting result. Used for starting system activities.
     *
     * @param route screen route to open and listening for result
     * @param animations animations for opening activity
     * @param activityOptions bundle with activity transition options
     */
    fun startForResult(
        route: ActivityWithResultRoute<*>,
        animations: Animations,
        activityOptions: Bundle?
    )

    /**
     * Request permission
     */
    fun requestPermission(route: PermissionRequestRoute)
}
