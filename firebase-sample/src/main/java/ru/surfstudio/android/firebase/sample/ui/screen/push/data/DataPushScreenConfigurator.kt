package ru.surfstudio.android.firebase.sample.ui.screen.push.data

import android.content.Intent
import dagger.Component
import dagger.Module
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.firebase.sample.ui.base.configurator.ActivityScreenConfigurator
import ru.surfstudio.android.firebase.sample.ui.base.dagger.activity.ActivityComponent
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.DefaultActivityScreenModule
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.DefaultCustomScreenModule

internal class DataPushScreenConfigurator(intent: Intent) : ActivityScreenConfigurator(intent) {
    @PerScreen
    @Component(dependencies = [ActivityComponent::class],
            modules = [DefaultActivityScreenModule::class, DataPushScreenModule::class])
    internal interface DataPushScreenComponent
        : ScreenComponent<DataPushActivityView>

    @Module
    internal class DataPushScreenModule(route: DataPushActivityRoute)
        : DefaultCustomScreenModule<DataPushActivityRoute>(route)

    override fun createScreenComponent(activityComponent: ActivityComponent,
                                       defaultActivityScreenModule: DefaultActivityScreenModule,
                                       intent: Intent): ScreenComponent<*> {
        return DaggerDataPushScreenConfigurator_DataPushScreenComponent.builder()
                .activityComponent(activityComponent)
                .defaultActivityScreenModule(defaultActivityScreenModule)
                .dataPushScreenModule(DataPushScreenModule(DataPushActivityRoute(intent)))
                .build()
    }
}