package ru.surfstudio.android.navigation.provider.container

import androidx.annotation.IdRes

/**
 * Interface for classes, which can host fragment navigation:
 * they must have a ViewGroup, that will be host for nested fragments.
 *
 * @property containerId id of hosting ViewGroup
 */
interface FragmentNavigationContainer {
    val containerId: Int @IdRes get
}