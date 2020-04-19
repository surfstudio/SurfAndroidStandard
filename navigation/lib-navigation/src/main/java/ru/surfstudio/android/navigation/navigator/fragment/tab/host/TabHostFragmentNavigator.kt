package ru.surfstudio.android.navigation.navigator.fragment.tab.host

import androidx.fragment.app.FragmentManager
import ru.surfstudio.android.navigation.navigator.fragment.FragmentNavigator

/**
 * Navigator that used in each host tab in [ru.surfstudio.android.navigation.navigator.fragment.tab.TabFragmentNavigator] mechanism
 */
class TabHostFragmentNavigator(
        fragmentManager: FragmentManager,
        containerId: Int,
        private val hostRouteTag: String
) : FragmentNavigator(fragmentManager, containerId) {

    internal fun obtainBackStack() = backStack

    override fun getBackStackKey() = "${super.getBackStackKey()} $hostRouteTag"

    override fun convertToBackStackTag(routeTag: String): String {
        val routeTagWithContainerId = super.convertToBackStackTag(routeTag)
        return "$hostRouteTag$ROUTE_TAG_DELIMITER$routeTagWithContainerId"
    }

    override fun extractRouteTag(backStackTag: String): String {
        return backStackTag.split(ROUTE_TAG_DELIMITER).last()
    }
}
