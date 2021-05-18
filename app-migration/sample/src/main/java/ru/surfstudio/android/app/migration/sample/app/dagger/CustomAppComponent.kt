package ru.surfstudio.android.app.migration.sample.app.dagger

import dagger.Component
import ru.surfstudio.android.app.migration.sample.app.initialization.InitializeAppInteractor
import ru.surfstudio.android.app.migration.sample.app.initialization.migration.MigrationModule
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.sample.dagger.app.dagger.DefaultAppComponent
import ru.surfstudio.android.sample.dagger.app.dagger.DefaultAppModule
import ru.surfstudio.android.sample.dagger.app.dagger.DefaultSharedPrefModule
import ru.surfstudio.android.sample.dagger.app.dagger.NavigationModule

@PerApplication
@Component(
    modules = [
        DefaultAppModule::class,
        DefaultSharedPrefModule::class,
        NavigationModule::class,
        MigrationModule::class
    ]
)
interface CustomAppComponent : DefaultAppComponent {
    fun initializeAppInteractor(): InitializeAppInteractor
}
