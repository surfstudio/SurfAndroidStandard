package ru.surfstudio.android.navigation.provider.container

/**
 * Interface for classes, which can host fragment navigation:
 * they must have a ViewGroup, that will be host for nested fragments.
 *
 * This interface is explicitly declares container for navigation with **tabs** (it will use
 * [ru.surfstudio.android.navigation.navigator.fragment.tab.TabFragmentNavigatorInterface] for
 * to perform navigation).
 *
 * @property containerId id of hosting ViewGroup
 */
interface TabFragmentNavigationContainer : FragmentNavigationContainer