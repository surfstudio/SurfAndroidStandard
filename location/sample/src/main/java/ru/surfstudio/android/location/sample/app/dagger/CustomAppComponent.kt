package ru.surfstudio.android.location.sample.app.dagger

import dagger.Component
import ru.surfstudio.android.dagger.scope.PerApplication
import ru.surfstudio.android.location.LocationService
import ru.surfstudio.android.location.sample.interactor.LocationModule
import ru.surfstudio.android.location.sample.ui.screen.common.CommonLocationPermissionRequest
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
        LocationModule::class
    ]
)
interface CustomAppComponent : DefaultAppComponent {
    fun locationService(): LocationService
    fun commonLocationPermissionRequest(): CommonLocationPermissionRequest
}
