package ru.surfstudio.android.easyadapter.sample.ui.screen.multitype

import android.content.Intent
import dagger.Component
import dagger.Module
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.easyadapter.sample.ui.base.configurator.ActivityScreenConfigurator
import ru.surfstudio.android.easyadapter.sample.ui.base.dagger.activity.ActivityComponent
import ru.surfstudio.android.easyadapter.sample.ui.base.dagger.screen.ActivityScreenModule
import ru.surfstudio.android.easyadapter.sample.ui.base.dagger.screen.CustomScreenModule

internal class MultitypeListScreenConfigurator(intent: Intent) : ActivityScreenConfigurator(intent) {
    @PerScreen
    @Component(dependencies = [ActivityComponent::class], modules = [ActivityScreenModule::class, MultitypeListScreenModule::class])
    internal interface MultitypeListScreenComponent
        : ScreenComponent<MultitypeListActivityView>

    @Module
    internal class MultitypeListScreenModule(route: MultitypeListActivityRoute)
        : CustomScreenModule<MultitypeListActivityRoute>(route)

    override fun createScreenComponent(activityComponent: ActivityComponent,
                                       activityScreenModule: ActivityScreenModule,
                                       intent: Intent): ScreenComponent<*> {
        return DaggerMultitypeListScreenConfigurator_MultitypeListScreenComponent.builder()
                .activityComponent(activityComponent)
                .activityScreenModule(activityScreenModule)
                .multitypeListScreenModule(MultitypeListScreenModule(MultitypeListActivityRoute()))
                .build()
    }
}