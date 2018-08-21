package ru.surfstudio.android.core.mvp.sample.ui.screen.another

import android.content.Intent
import dagger.Component
import dagger.Module
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.core.mvp.sample.ui.base.configurator.ActivityScreenConfigurator
import ru.surfstudio.android.core.mvp.sample.ui.base.dagger.activity.ActivityComponent
import ru.surfstudio.android.core.mvp.sample.ui.base.dagger.screen.ActivityScreenModule
import ru.surfstudio.android.core.mvp.sample.ui.base.dagger.screen.CustomScreenModule
import ru.surfstudio.android.dagger.scope.PerScreen

internal class AnotherScreenConfigurator(intent: Intent) : ActivityScreenConfigurator(intent) {
    @PerScreen
    @Component(dependencies = [ActivityComponent::class],
            modules = [ActivityScreenModule::class, AnotherScreenModule::class])
    internal interface AnotherScreenComponent
        : ScreenComponent<AnotherActivityView>

    @Module
    internal class AnotherScreenModule(route: AnotherActivityRoute)
        : CustomScreenModule<AnotherActivityRoute>(route)

    override fun createScreenComponent(activityComponent: ActivityComponent,
                                       activityScreenModule: ActivityScreenModule,
                                       intent: Intent): ScreenComponent<*> {
        return DaggerAnotherScreenConfigurator_AnotherScreenComponent.builder()
                .activityComponent(activityComponent)
                .activityScreenModule(activityScreenModule)
                .anotherScreenModule(AnotherScreenModule(AnotherActivityRoute()))
                .build()
    }
}