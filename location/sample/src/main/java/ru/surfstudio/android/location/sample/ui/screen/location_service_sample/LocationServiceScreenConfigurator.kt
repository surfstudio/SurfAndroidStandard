package ru.surfstudio.android.location.sample.ui.screen.location_service_sample

import android.content.Intent
import dagger.Component
import dagger.Module
import dagger.Provides
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager
import ru.surfstudio.android.core.ui.permission.PermissionManager
import ru.surfstudio.android.core.ui.provider.ActivityProvider
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.location.ILocationService
import ru.surfstudio.android.location.sample.ui.base.configurator.CustomActivityScreenConfigurator
import ru.surfstudio.android.location.sample.ui.base.dagger.activity.CustomActivityComponent
import ru.surfstudio.android.location.sample.ui.screen.common.CommonLocationPermissionRequest
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.DefaultActivityScreenModule

/**
 * Конфигуратор экрана [LocationServiceActivityView]
 */
class LocationServiceScreenConfigurator(intent: Intent) : CustomActivityScreenConfigurator(intent) {

    @PerScreen
    @Component(dependencies = [CustomActivityComponent::class],
            modules = [DefaultActivityScreenModule::class, LocationServiceScreenModule::class])
    internal interface LocationServiceScreenComponent : ScreenComponent<LocationServiceActivityView>

    @Module
    internal class LocationServiceScreenModule {

        @Provides
        internal fun provideLocationServicePresenter(
            basePresenterDependency: BasePresenterDependency,
            screenEventDelegateManager: ScreenEventDelegateManager,
            permissionManager: PermissionManager,
            activityProvider: ActivityProvider,
            commonLocationPermissionRequest: CommonLocationPermissionRequest,
            locationService: ILocationService
        ) = LocationServicePresenter(
                basePresenterDependency,
                screenEventDelegateManager,
                permissionManager,
                activityProvider,
                commonLocationPermissionRequest,
                locationService
        )
    }

    override fun createScreenComponent(
            customActivityComponent: CustomActivityComponent,
            defaultActivityScreenModule: DefaultActivityScreenModule,
            intent: Intent): ScreenComponent<*> {
        return DaggerLocationServiceScreenConfigurator_LocationServiceScreenComponent.builder()
                .customActivityComponent(customActivityComponent)
                .defaultActivityScreenModule(defaultActivityScreenModule)
                .locationServiceScreenModule(LocationServiceScreenModule())
                .build()
    }
}