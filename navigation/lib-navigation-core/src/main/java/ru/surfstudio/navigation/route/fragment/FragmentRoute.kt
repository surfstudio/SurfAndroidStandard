package ru.surfstudio.navigation.route.fragment

import androidx.fragment.app.Fragment
import ru.surfstudio.navigation.route.BaseRoute

/**
 * Route for [Fragment].
 */
open class FragmentRoute : BaseRoute<Fragment>() {

    /**
     * Creates Fragment with [getScreenClass] or [getScreenClassPath] class.
     */
    open fun createFragment(): Fragment {
        return requireScreenClass().newInstance()
                .apply { prepareData()?.let(::setArguments) }
    }
}