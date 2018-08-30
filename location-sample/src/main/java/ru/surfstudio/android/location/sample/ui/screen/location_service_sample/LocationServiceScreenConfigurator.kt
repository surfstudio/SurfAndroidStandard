package ru.surfstudio.android.location.sample.ui.screen.location_service_sample

import android.content.Context
import android.content.Intent
import dagger.Component
import dagger.Module
import dagger.Provides
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager
import ru.surfstudio.android.core.ui.permission.PermissionManager
import ru.surfstudio.android.core.ui.provider.ActivityProvider
import ru.surfstudio.android.core.ui.state.ScreenState
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.location.LocationService
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProvider
import ru.surfstudio.android.sample.dagger.ui.base.configurator.DefaultActivityScreenConfigurator
import ru.surfstudio.android.sample.dagger.ui.base.dagger.activity.DefaultActivityComponent
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.DefaultActivityScreenModule

/**
 * Конфигуратор экрана [LocationServiceActivityView]
 */
class LocationServiceScreenConfigurator(intent: Intent) : DefaultActivityScreenConfigurator(intent) {

    @PerScreen
    @Component(dependencies = [DefaultActivityComponent::class],
            modules = [DefaultActivityScreenModule::class, LocationServiceScreenModule::class])
    internal interface LocationServiceScreenComponent : ScreenComponent<LocationServiceActivityView>

    @Module
    internal class LocationServiceScreenModule {

        @Provides
        fun provideLocationService(context: Context) = LocationService(context)

        @Provides
        fun provideLocationServicePresenter(
                screenEventDelegateManager: ScreenEventDelegateManager,
                screenState: ScreenState,
                permissionManager: PermissionManager,
                activityProvider: ActivityProvider,
                schedulersProvider: SchedulersProvider,
                locationService: LocationService
        ) = LocationServicePresenter(
                screenEventDelegateManager,
                screenState,
                permissionManager,
                activityProvider,
                schedulersProvider,
                locationService
        )
    }

    override fun createScreenComponent(defaultActivityComponent: DefaultActivityComponent,
                                       defaultActivityScreenModule: DefaultActivityScreenModule,
                                       intent: Intent): ScreenComponent<*> {
        return DaggerLocationServiceScreenConfigurator_LocationServiceScreenComponent.builder()
                .defaultActivityComponent(defaultActivityComponent)
                .defaultActivityScreenModule(defaultActivityScreenModule)
                .locationServiceScreenModule(LocationServiceScreenModule())
                .build()
    }
}