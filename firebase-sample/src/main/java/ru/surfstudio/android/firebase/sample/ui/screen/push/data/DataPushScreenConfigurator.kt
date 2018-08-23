package ru.surfstudio.android.firebase.sample.ui.screen.push.data

import android.content.Intent
import dagger.Component
import dagger.Module
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.firebase.sample.ui.base.configurator.ActivityScreenConfigurator
import ru.surfstudio.android.firebase.sample.ui.base.dagger.activity.ActivityComponent
import ru.surfstudio.android.firebase.sample.ui.base.dagger.screen.ActivityScreenModule
import ru.surfstudio.android.firebase.sample.ui.base.dagger.screen.CustomScreenModule

internal class DataPushScreenConfigurator(intent: Intent) : ActivityScreenConfigurator(intent) {
    @PerScreen
    @Component(dependencies = [ActivityComponent::class],
            modules = [ActivityScreenModule::class, DataPushScreenModule::class])
    internal interface DataPushScreenComponent
        : ScreenComponent<DataPushActivityView>

    @Module
    internal class DataPushScreenModule(route: DataPushActivityRoute)
        : CustomScreenModule<DataPushActivityRoute>(route)

    override fun createScreenComponent(parentActivityComponent: ActivityComponent?,
                                       activityScreenModule: ActivityScreenModule?,
                                       intent: Intent): ScreenComponent<*> {
        return DaggerDataPushScreenConfigurator_DataPushScreenComponent.builder()
                .activityComponent(activityComponent)
                .activityScreenModule(activityScreenModule)
                .dataPushScreenModule(DataPushScreenModule(DataPushActivityRoute(intent)))
                .build()
    }
}