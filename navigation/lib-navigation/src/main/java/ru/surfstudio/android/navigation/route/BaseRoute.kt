package ru.surfstudio.android.navigation.route

import android.os.Bundle
import ru.surfstudio.android.navigation.route.error.RouteClassPathFoundException
import ru.surfstudio.android.navigation.route.error.RouteScreenNotDeclaredException

/**
 * ## Base route interface implementation that is bound to the specific screen.
 *
 * [S] base class from which desired class should be inherited from.
 */
abstract class BaseRoute<S> : Route {

    /**
     * Gets class of the screen, which should be resolved.
     *
     * You should override this method if your route is placed in a single module together with a screen,
     * or you have access to a screen class.
     */
    open fun getScreenClass(): Class<out S>? = null

    /**
     * Gets path to the screen class from which screen will be resolved.
     *
     * You should override this method if your route has no access to a screen class.
     */
    open fun getScreenClassPath(): String? = null

    /**
     * Creates the data payload that will be passed to a screen during its initialization
     *
     * You should override this method if data needs to be passed to the screen.
     */
    open fun prepareData(): Bundle = Bundle()

    /**
     * Returns the string which is used to uniquely identify the screen.
     */
    open fun getId(): String = requireScreenClass().canonicalName

    /**
     * Inflates the data payload that will be passed to a screen during its initialization with
     * screen identifier from [getId] method.
     *
     * This method can't be overwritten.
     */
    protected fun prepareDataWithId(): Bundle {
        val dataBundle = prepareData()
        dataBundle.putString(Route.EXTRA_SCREEN_ID, getId())
        return dataBundle
    }

    private fun requireScreenClassPath(): Class<out S>? {
        val classPath = getScreenClassPath()
        return try {
            Class.forName(classPath) as? Class<out S>
        } catch (e: ClassNotFoundException) {
            throw RouteClassPathFoundException(classPath)
        }
    }

    protected fun requireScreenClass(): Class<out S> {
        return getScreenClass()
                ?: requireScreenClassPath()
                ?: throw RouteScreenNotDeclaredException()
    }

}