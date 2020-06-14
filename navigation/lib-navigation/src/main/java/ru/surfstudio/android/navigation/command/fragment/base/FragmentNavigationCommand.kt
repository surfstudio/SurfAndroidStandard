package ru.surfstudio.android.navigation.command.fragment.base

import ru.surfstudio.android.navigation.command.NavigationCommand
import ru.surfstudio.android.navigation.route.fragment.FragmentRoute

/**
 * Command that is used in navigation process with [androidx.fragment.app.Fragment]s.
 */
interface FragmentNavigationCommand : NavigationCommand {

    /**
     * Route, that is used to:
     *
     * * Identify fragment: [FragmentRoute.getTag]
     * * Create new fragment instance: [FragmentRoute.createFragment]
     */
    override val route: FragmentRoute

    /**
     * Tag of a source screen, which will execute navigation command.
     *
     * If a source screen is activity, you may just ignore this option
     *
     * If a source screen is fragment, you must pass
     * [androidx.fragment.app.Fragment.getTag] as a source tag,
     * or pass [ACTIVITY_NAVIGATION_TAG] to explicitly execute command on top level.
     *
     * Source screen is usually just a screen, that calls navigation methods, but not always:
     * you may need to execute navigation command for a current fragment's parent,
     * then you should pass parent tag as an option.
     */
    var sourceTag: String

    companion object {
        const val ACTIVITY_NAVIGATION_TAG = "activity_navigation_tag"
    }
}