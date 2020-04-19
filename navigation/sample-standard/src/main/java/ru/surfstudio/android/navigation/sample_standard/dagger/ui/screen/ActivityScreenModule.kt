package ru.surfstudio.android.navigation.sample_standard.dagger.ui.screen

import dagger.Module
import dagger.Provides
import ru.surfstudio.android.core.ui.scope.ScreenPersistentScope
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.navigation.executor.AppCommandExecutor
import ru.surfstudio.android.navigation.executor.NavigationCommandExecutor
import ru.surfstudio.android.navigation.provider.FragmentProvider
import ru.surfstudio.android.navigation.executor.screen.ScreenCommandExecutor
import ru.surfstudio.android.navigation.sample_standard.screen.base.FragmentProviderImpl

@Module
open class ActivityScreenModule {

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
        return ScreenCommandExecutor(fragmentProvider, appCommandExecutor)
    }
}