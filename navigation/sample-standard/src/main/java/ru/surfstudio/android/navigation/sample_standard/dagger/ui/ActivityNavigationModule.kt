package ru.surfstudio.android.navigation.sample_standard.dagger.ui

import dagger.Module
import dagger.Provides
import ru.surfstudio.android.dagger.scope.PerActivity
import ru.surfstudio.android.navigation.provider.ActivityNavigationProvider
import ru.surfstudio.android.navigation.provider.FragmentNavigationProvider

@Module
class ActivityNavigationModule {

    @Provides
    @PerActivity
    fun provideFragmentNavigationProvider(activityNavigationProvider: ActivityNavigationProvider): FragmentNavigationProvider {
        return activityNavigationProvider.provide().fragmentNavigationProvider
    }
}