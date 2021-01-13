package ru.surfstudio.android.navigation.observer.navigator.activity

import android.os.Bundle
import ru.surfstudio.android.navigation.animation.Animations
import ru.surfstudio.android.navigation.navigator.activity.ActivityNavigatorInterface
import ru.surfstudio.android.navigation.observer.listener.ScreenResultListener
import ru.surfstudio.android.navigation.observer.route.ActivityWithResultRoute
import java.io.Serializable

/**
 *  Interface for an activity navigator with method start for result.
 */
interface ActivityNavigatorWithResultInterface : ActivityNavigatorInterface {

    /**
     * Registers callback for onActivityResult
     *
     * @param route route of screen which should handle the result
     * @param callback callback which will be called on activity result.
     * callback will be called only if result returned from ActivityWithResultRoute#parseResult is not null
     */
    fun <T : Serializable> callbackResult(
            route: ActivityWithResultRoute<T>,
            callback: ScreenResultListener<T>
    )

    /**
     * Starts a new activity for getting result. Used for starting system activities.
     *
     * @param route screen route to open and listening for result
     * @param animations animations for opening activity
     * @param activityOptions bundle with activity transition options
     */
    fun startForResult(route: ActivityWithResultRoute<*>, animations: Animations, activityOptions: Bundle?)
}