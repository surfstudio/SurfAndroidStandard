package ru.surfstudio.android.navigation.sample_standard.di

import dagger.Component
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.navigation.observer.ScreenResultObserver
import ru.surfstudio.android.navigation.provider.ActivityNavigationProvider
import ru.surfstudio.android.sample.dagger.app.dagger.DefaultAppComponent
import ru.surfstudio.android.sample.dagger.app.dagger.DefaultAppModule
import ru.surfstudio.android.sample.dagger.app.dagger.DefaultSharedPrefModule

@PerApplication
@Component(
    modules = [
        DefaultAppModule::class,
        DefaultSharedPrefModule::class,
        AppNavigationModule::class
    ]
)
interface AppComponent : DefaultAppComponent {
    fun activityNavigationProvider(): ActivityNavigationProvider
    fun screenResultObserver(): ScreenResultObserver
}

