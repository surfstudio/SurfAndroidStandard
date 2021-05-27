package ru.surfstudio.android.navigation.scope

import ru.surfstudio.android.navigation.provider.ActivityNavigationProvider
import ru.surfstudio.android.navigation.provider.FragmentProvider
import ru.surfstudio.android.navigation.provider.holder.ActivityNavigationHolder
import ru.surfstudio.android.navigation.provider.holder.FragmentNavigationHolder

/**
 * Provider for navigation entities in retained screen scope.
 *
 * It can be used because [ActivityNavigationHolder] and [FragmentNavigationHolder] are living with
 * their screens and cannot survive configuration changes, so they shouldn't be provided directly to
 * presenters or viewmodels, which survive configuration changes.
 *
 * As a workaround, you can use this class in your DI mechanism
 * to provide actual holders for entities that survive configuration changes
 * without creating potential memory leaks.
 */
class ScreenScopeNavigationProvider(
        private val fragmentProvider: FragmentProvider,
        private val activityNavigationProvider: ActivityNavigationProvider
) {

    fun getActivityNavigationHolder(): ActivityNavigationHolder =
            activityNavigationProvider.provide()

    fun getFragmentNavigationHolder(): FragmentNavigationHolder =
            getActivityNavigationHolder()
                    .fragmentNavigationProvider
                    .provide(fragmentProvider.provide()?.tag)
}