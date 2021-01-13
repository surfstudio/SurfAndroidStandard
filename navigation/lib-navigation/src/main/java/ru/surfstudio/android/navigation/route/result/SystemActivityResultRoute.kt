package ru.surfstudio.android.navigation.route.result

import android.content.Intent
import ru.surfstudio.android.navigation.route.activity.ActivityRoute
import ru.surfstudio.android.navigation.route.error.RouteClassPathFoundException
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

    override fun getId(): String {
        return getScreenClass()?.canonicalName
                ?: requireScreenClassPath()?.canonicalName
                ?: "${this.javaClass.name}${screenId.hashCode()} "
    }


    private fun requireScreenClassPath(): Class<out T>? {
        val classPath = getScreenClassPath()
        return try {
            Class.forName(classPath) as? Class<out T>
        } catch (e: ClassNotFoundException) {
            throw RouteClassPathFoundException(classPath)
        } catch (e: NullPointerException) {
            null
        }
    }

    abstract fun parseResultIntent(resultIntent: Intent?): T

    fun getRequestCode(): Int {
        return Math.abs(this.javaClass.canonicalName!!.hashCode() % MAX_REQUEST_CODE)
    }
}