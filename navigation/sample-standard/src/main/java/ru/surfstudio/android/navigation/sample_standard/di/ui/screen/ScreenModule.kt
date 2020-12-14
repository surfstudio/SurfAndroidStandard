package ru.surfstudio.android.navigation.sample_standard.di.ui.screen

import dagger.Module
import dagger.Provides
import ru.surfstudio.android.core.ui.scope.ScreenPersistentScope
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.navigation.executor.AppCommandExecutor
import ru.surfstudio.android.navigation.executor.NavigationCommandExecutor
import ru.surfstudio.android.navigation.provider.ActivityNavigationProvider
import ru.surfstudio.android.navigation.provider.FragmentProvider
import ru.surfstudio.android.navigation.scope.ScreenScopeCommandExecutor
import ru.surfstudio.android.navigation.sample_standard.screen.base.provider.FragmentProviderImpl
import ru.surfstudio.android.navigation.scope.ScreenScopeNavigationProvider

@Module
open class ScreenModule {

    @Provides
    @PerScreen
    fun provideFragmentProvider(persistentScope: ScreenPersistentScope): FragmentProvider {
        return FragmentProviderImpl(persistentScope)
    }

    @Provides
    @PerScreen
    fun provideCommandExecutor(
            fragmentProvider: FragmentProvider,
            appCommandExecutor: AppCommandExecutor
    ): NavigationCommandExecutor {
        return ScreenScopeCommandExecutor(fragmentProvider, appCommandExecutor)
    }

    @Provides
    @PerScreen
    fun provideScreenNavigationProvider(
            fragmentProvider: FragmentProvider,
            activityNavigationProvider: ActivityNavigationProvider
    ): ScreenScopeNavigationProvider {
        return ScreenScopeNavigationProvider(fragmentProvider, activityNavigationProvider)
    }
}