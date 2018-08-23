package ru.surfstudio.android.firebase.sample.ui.screen.push

import android.content.Intent
import dagger.Component
import dagger.Module
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.firebase.sample.ui.base.configurator.ActivityScreenConfigurator
import ru.surfstudio.android.firebase.sample.ui.base.dagger.activity.ActivityComponent
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.DefaultActivityScreenModule
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.DefaultCustomScreenModule

internal class PushScreenConfigurator(intent: Intent) : ActivityScreenConfigurator(intent) {
    @PerScreen
    @Component(dependencies = [ActivityComponent::class],
            modules = [DefaultActivityScreenModule::class, PushScreenModule::class])
    internal interface PushScreenComponent
        : ScreenComponent<PushActivityView>

    @Module
    internal class PushScreenModule(route: PushActivityRoute)
        : DefaultCustomScreenModule<PushActivityRoute>(route)

    override fun createScreenComponent(activityComponent: ActivityComponent,
                                       defaultActivityScreenModule: DefaultActivityScreenModule,
                                       intent: Intent): ScreenComponent<*> {
        return DaggerPushScreenConfigurator_PushScreenComponent.builder()
                .activityComponent(activityComponent)
                .defaultActivityScreenModule(defaultActivityScreenModule)
                .pushScreenModule(PushScreenModule(PushActivityRoute()))
                .build()
    }
}