package ru.surfstudio.android.navigation.observer.route

import android.content.Intent
import ru.surfstudio.android.navigation.route.activity.ActivityRoute
import java.io.Serializable

/**
 * Route for activity with result [ActivityRoute]
 * You should use this route when launching system screens.
 *
 * @param <T> тип результата
 */
abstract class ActivityWithResultRoute<T : Serializable> : ActivityRoute(), ResultRoute<T> {

    override fun getId(): String {
        return this.javaClass.canonicalName
    }

    /**
     * Parsing result Intent to given type.
     */
    abstract fun parseResultIntent(resultCode: Int, resultIntent: Intent?): T
}