package ru.surfstudio.android.navigation.provider.holder

import ru.surfstudio.android.navigation.navigator.activity.ActivityNavigatorInterface
import ru.surfstudio.android.navigation.navigator.dialog.DialogNavigatorInterface
import ru.surfstudio.android.navigation.provider.FragmentNavigationProvider

/**
 * Holder class that keeps all entities for activity navigation process.
 */
open class ActivityNavigationHolder(
        val activityNavigator: ActivityNavigatorInterface,
        val dialogNavigator: DialogNavigatorInterface,
        val fragmentNavigationProvider: FragmentNavigationProvider
)