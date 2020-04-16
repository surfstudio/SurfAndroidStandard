package ru.surfstudio.android.navigation.supplier.holder

import ru.surfstudio.android.navigation.supplier.FragmentNavigationSupplier
import ru.surfstudio.android.navigation.navigator.activity.ActivityNavigatorInterface
import ru.surfstudio.android.navigation.navigator.dialog.DialogNavigatorInterface

open class ActivityNavigationHolder(
        val activityNavigator: ActivityNavigatorInterface,
        val dialogNavigator: DialogNavigatorInterface,
        val fragmentSupplier: FragmentNavigationSupplier
)