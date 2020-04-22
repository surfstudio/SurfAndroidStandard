package ru.surfstudio.android.navigation.sample_standard.dagger

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.navigation.executor.AppCommandExecutor
import ru.surfstudio.android.navigation.provider.ActivityNavigationProvider
import ru.surfstudio.android.navigation.provider.callbacks.ActivityNavigationProviderCallbacks
import ru.surfstudio.android.navigation.sample_standard.App

@Module
class AppNavigationModule {

    @Provides
    @PerApplication
    fun provideActivityNavigationProvider(
            activityNavigationProviderCallbacks: ActivityNavigationProviderCallbacks
    ): ActivityNavigationProvider {
        return activityNavigationProviderCallbacks
    }


    @Provides
    @PerApplication
    fun provideActivityNavigationProviderCallbacks(context: Context): ActivityNavigationProviderCallbacks {
        return ActivityNavigationProviderCallbacks()
    }

    @Provides
    @PerApplication
    fun provideAppCommandExecutor(activityNavigationProvider: ActivityNavigationProvider): AppCommandExecutor {
        return AppCommandExecutor(activityNavigationProvider)
    }
}