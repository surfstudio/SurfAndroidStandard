package ru.surfstudio.android.navigation.navigator.fragment.tab.host

import androidx.fragment.app.FragmentManager
import ru.surfstudio.android.navigation.navigator.fragment.FragmentNavigator

/**
 * Navigator that used in each host tab in [TabFragmentNavigator] mechanism
 */
class TabHostFragmentNavigator(
        override val fragmentManager: FragmentManager,
        override val containerId: Int,
        private val hostRouteTag: String
) : FragmentNavigator(fragmentManager, containerId) {

    internal fun obtainBackStack() = backStack

    override fun getBackStackKey() = "${super.getBackStackKey()} $hostRouteTag"
}
