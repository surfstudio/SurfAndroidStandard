package ru.surfstudio.standard.ui.screen_modules

import dagger.Module
import dagger.Provides
import ru.surfstudio.android.core.ui.scope.ScreenPersistentScope
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.navigation.executor.AppCommandExecutor
import ru.surfstudio.android.navigation.executor.NavigationCommandExecutor
import ru.surfstudio.android.navigation.provider.FragmentProvider
import ru.surfstudio.android.navigation.scope.ScreenScopeCommandExecutor
import ru.surfstudio.standard.ui.provider.FragmentProviderImpl

/**
 * Модуль с зависимостями навигации MVI.
 */
@Module
open class NavigationScreenModule {

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
}
