package ru.surfstudio.android.navigation.route.widget

import android.content.Context
import android.view.ViewGroup
import ru.surfstudio.android.navigation.route.BaseRoute

/**
 * Route for [Widget].
 */
@Deprecated("Feature is not supported yet")
open class WidgetRoute : BaseRoute<ViewGroup>() {

    /**
     * Creates Widget with [getScreenClass] or [getScreenClassPath] class.
     */
    open fun createWidget(context: Context): ViewGroup {
        val constructor = requireScreenClass().getConstructor(Context::class.java)
        return constructor.newInstance(context) as ViewGroup
    }
}