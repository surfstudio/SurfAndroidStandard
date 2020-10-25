package ru.surfstudio.android.navigation.route.dialog

import androidx.fragment.app.DialogFragment
import ru.surfstudio.android.navigation.route.BaseRoute

/**
 * Route for [DialogFragment].
 */
open class DialogRoute : BaseRoute<DialogFragment>() {

    /**
     * Creates DialogFragment with [getScreenClass] or [getScreenClassPath] class.
     */
    open fun createDialog(): DialogFragment {
        return requireScreenClass().newInstance()
                .apply { arguments = prepareDataWithId() }
    }
}