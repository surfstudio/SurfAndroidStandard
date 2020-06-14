package ru.surfstudio.android.navigation.provider.holder

import ru.surfstudio.android.navigation.provider.FragmentNavigationProvider
import ru.surfstudio.android.navigation.navigator.activity.ActivityNavigatorInterface
import ru.surfstudio.android.navigation.navigator.dialog.DialogNavigatorInterface

open class ActivityNavigationHolder(
        val activityNavigator: ActivityNavigatorInterface,
        val dialogNavigator: DialogNavigatorInterface,
        val fragmentNavigationProvider: FragmentNavigationProvider
)