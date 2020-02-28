package ru.surfstudio.android.core.mvi.sample.ui.base.di

import dagger.Module
import dagger.Provides
import ru.surfstudio.android.core.mvi.impls.ui.middleware.BaseMiddlewareDependency
import ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation.composition.NavigationMiddleware
import ru.surfstudio.android.core.mvi.impls.ui.middleware.navigation.ScreenNavigator
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.core.ui.navigation.fragment.FragmentNavigator
import ru.surfstudio.android.core.ui.provider.ActivityProvider
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.mvp.dialog.navigation.navigator.DialogNavigator

/**
 * Модуль с зависимостями навигации
 */
@Module
class NavigationScreenModule {
    @PerScreen
    @Provides
    fun provideNavigationMiddleware(
            baseMiddlewareDependency: BaseMiddlewareDependency,
            screenNavigator: ScreenNavigator
    ): NavigationMiddleware = NavigationMiddleware(baseMiddlewareDependency, screenNavigator)

    @PerScreen
    @Provides
    fun provideScreenNavigator(
            activityNavigator: ActivityNavigator,
            fragmentNavigator: FragmentNavigator,
            dialogNavigator: DialogNavigator
    ): ScreenNavigator {
        return ScreenNavigator(activityNavigator, fragmentNavigator, dialogNavigator)
    }


    @Provides
    @PerScreen
    fun provideFragmentNavigator(activityProvider: ActivityProvider): FragmentNavigator = FragmentNavigator(activityProvider)
}