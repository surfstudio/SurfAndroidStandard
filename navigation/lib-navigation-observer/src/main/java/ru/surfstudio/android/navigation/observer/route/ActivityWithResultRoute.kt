package ru.surfstudio.android.navigation.observer.route

import android.content.Intent
import ru.surfstudio.android.navigation.route.activity.ActivityRoute
import java.io.Serializable

/**
 * Route for activity with result [ActivityRoute]
 * You should use this route when launching system screens.
 *
 * @param <T> type of the result returned from activity
 */
abstract class ActivityWithResultRoute<T : Serializable> : ActivityRoute(), ResultRoute<T> {

    /**
     * Screen identifier, used to avoid collisions where one result can be returned to many subscribers.
     * Result of the activity will be returned only to subscribers which uniqueId is the same.
     */
    abstract val uniqueId: String

    override fun getId(): String {
        return "${this.javaClass.canonicalName}$uniqueId"
    }

    /**
     * Parsing result Intent to given type.
     */
    abstract fun parseResultIntent(resultCode: Int, resultIntent: Intent?): T
}