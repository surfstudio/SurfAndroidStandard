package ru.surfstudio.android.navigation.route.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import ru.surfstudio.android.navigation.route.BaseRoute
import ru.surfstudio.android.navigation.route.Route.Companion.SCREEN_ID

/**
 * Route for [Fragment].
 */
open class FragmentRoute : BaseRoute<Fragment>() {

    /**
     * Creates Fragment with [getScreenClass] or [getScreenClassPath] class.
     */
    open fun createFragment(): Fragment {
        //TODO подумать, хорошая ли идея класть сюда SCREEN_ID для идентификации экрана
        val routeTag = getTag()
        return requireScreenClass().newInstance()
                .apply {
                    arguments = Bundle().apply {
                        putString(SCREEN_ID, routeTag)
                        prepareData()?.let(::putAll)
                    }
                }
    }
}