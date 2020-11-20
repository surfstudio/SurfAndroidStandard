package ru.surfstudio.android.location.sample.ui.base.dagger.activity

import dagger.Component
import ru.surfstudio.android.dagger.scope.PerActivity
import ru.surfstudio.android.location.LocationService
import ru.surfstudio.android.location.sample.app.dagger.CustomAppComponent
import ru.surfstudio.android.location.sample.ui.screen.common.CommonLocationPermissionRequest
import ru.surfstudio.android.sample.dagger.ui.base.dagger.activity.DefaultActivityComponent
import ru.surfstudio.android.sample.dagger.ui.base.dagger.activity.DefaultActivityModule

/**
 * Компонент для @PerActivity скоупа
 */
@PerActivity
@Component(dependencies = [(CustomAppComponent::class)],
        modules = [(DefaultActivityModule::class)])
interface CustomActivityComponent : DefaultActivityComponent {
    fun locationService(): LocationService
    fun commonLocationPermissionRequest(): CommonLocationPermissionRequest
}