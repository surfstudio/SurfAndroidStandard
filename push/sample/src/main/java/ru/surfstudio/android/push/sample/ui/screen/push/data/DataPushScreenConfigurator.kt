package ru.surfstudio.android.push.sample.ui.screen.push.data

import android.content.Intent
import dagger.Component
import dagger.Module
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.push.sample.ui.base.configurator.CustomActivityScreenConfigurator
import ru.surfstudio.android.push.sample.ui.base.dagger.activity.CustomActivityComponent
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.DefaultActivityScreenModule
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.DefaultCustomScreenModule

internal class DataPushScreenConfigurator(intent: Intent) : CustomActivityScreenConfigurator(intent) {
    @PerScreen
    @Component(dependencies = [CustomActivityComponent::class],
            modules = [DefaultActivityScreenModule::class, DataPushScreenModule::class])
    internal interface DataPushScreenComponent
        : ScreenComponent<DataPushActivityView>

    @Module
    internal class DataPushScreenModule(route: DataPushActivityRoute)
        : DefaultCustomScreenModule<DataPushActivityRoute>(route)

    override fun createScreenComponent(customActivityComponent: CustomActivityComponent,
                                       defaultActivityScreenModule: DefaultActivityScreenModule,
                                       intent: Intent): ScreenComponent<*> {
        return DaggerDataPushScreenConfigurator_DataPushScreenComponent.builder()
                .customActivityComponent(customActivityComponent)
                .defaultActivityScreenModule(defaultActivityScreenModule)
                .dataPushScreenModule(DataPushScreenModule(DataPushActivityRoute(intent)))
                .build()
    }
}