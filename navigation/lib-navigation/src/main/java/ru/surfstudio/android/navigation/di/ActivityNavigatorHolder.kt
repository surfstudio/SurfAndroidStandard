package ru.surfstudio.android.navigation.di

import ru.surfstudio.android.navigation.navigator.activity.ActivityNavigatorInterface

open class ActivityNavigatorHolder(
        val activityNavigator: ActivityNavigatorInterface,
        val nestedNavigationSupplier: FragmentNavigationSupplier
        //todo dialog navigator
)