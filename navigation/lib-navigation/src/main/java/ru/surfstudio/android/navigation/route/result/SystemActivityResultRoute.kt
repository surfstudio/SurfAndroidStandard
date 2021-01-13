package ru.surfstudio.android.navigation.route.result

import android.content.Intent
import ru.surfstudio.android.navigation.route.activity.ActivityRoute
import java.io.Serializable

private const val MAX_REQUEST_CODE = 32768

/**
 * Route for activity result
 */
abstract class SystemActivityResultRoute<T : Serializable> : ActivityRoute(), ResultRoute<ActivityResultData<T>> {

    /**
     * Screen identifier, used to avoid collisions
     */
    abstract val screenId: String

    abstract fun parseResultIntent(resultIntent: Intent?): T

    override fun getId(): String {
        return "${this.javaClass.name}${screenId.hashCode()}"
    }

    open fun getRequestCode(): Int {
        return Math.abs(this.javaClass.canonicalName!!.hashCode() % MAX_REQUEST_CODE)
    }
}