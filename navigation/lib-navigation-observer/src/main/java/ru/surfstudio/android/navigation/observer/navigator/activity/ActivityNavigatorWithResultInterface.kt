package ru.surfstudio.android.navigation.observer.navigator.activity

import android.os.Bundle
import ru.surfstudio.android.navigation.animation.Animations
import ru.surfstudio.android.navigation.navigator.activity.ActivityNavigatorInterface
import ru.surfstudio.android.navigation.observer.route.ActivityWithResultRoute
import java.io.Serializable

/**
 *  Base interface for an activity navigator.
 */
interface ActivityNavigatorWithResultInterface : ActivityNavigatorInterface {

    fun startForResult(route: ActivityWithResultRoute<*>, animations: Animations, activityOptions: Bundle?)

    fun <T : Serializable> callbackResult(
            route: ActivityWithResultRoute<T>,
            callback: ResultCallback<T>
    )

    interface ResultCallback<T : Serializable> {
        fun onActivityResult(data: T)
    }
}