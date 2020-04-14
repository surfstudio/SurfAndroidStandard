package ru.surfstudio.android.navigation.di.supplier.holder

import ru.surfstudio.android.navigation.di.supplier.FragmentNavigationSupplier
import ru.surfstudio.android.navigation.navigator.activity.ActivityNavigatorInterface
import ru.surfstudio.android.navigation.navigator.dialog.DialogNavigatorInterface

open class ActivityNavigationHolder(
        val activityNavigator: ActivityNavigatorInterface,
        val dialogNavigator: DialogNavigatorInterface,
        val nestedNavigationSupplier: FragmentNavigationSupplier
)