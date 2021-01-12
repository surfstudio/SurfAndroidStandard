package ru.surfstudio.android.navigation.observer.provider

import ru.surfstudio.android.navigation.navigator.dialog.DialogNavigatorInterface
import ru.surfstudio.android.navigation.observer.navigator.activity.ActivityNavigatorWithResultInterface
import ru.surfstudio.android.navigation.provider.FragmentNavigationProvider
import ru.surfstudio.android.navigation.provider.holder.ActivityNavigationHolder

/**
 * Holder class that keeps all entities for activity navigation process.
 */
open class ActivityNavigationWithResultHolder(
        activityNavigator: ActivityNavigatorWithResultInterface,
        dialogNavigator: DialogNavigatorInterface,
        fragmentNavigationProvider: FragmentNavigationProvider
) : ActivityNavigationHolder(activityNavigator, dialogNavigator, fragmentNavigationProvider)
