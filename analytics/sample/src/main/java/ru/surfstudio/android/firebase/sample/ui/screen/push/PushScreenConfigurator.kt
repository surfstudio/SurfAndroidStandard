package ru.surfstudio.android.firebase.sample.ui.screen.push

import android.content.Intent
import dagger.Component
import dagger.Module
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.firebase.sample.ui.base.configurator.CustomActivityScreenConfigurator
import ru.surfstudio.android.firebase.sample.ui.base.dagger.activity.CustomActivityComponent
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.DefaultActivityScreenModule
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.DefaultCustomScreenModule

internal class PushScreenConfigurator(intent: Intent) : CustomActivityScreenConfigurator(intent) {
    @PerScreen
    @Component(dependencies = [CustomActivityComponent::class],
            modules = [DefaultActivityScreenModule::class, PushScreenModule::class])
    internal interface PushScreenComponent
        : ScreenComponent<PushActivityView>

    @Module
    internal class PushScreenModule(route: PushActivityRoute)
        : DefaultCustomScreenModule<PushActivityRoute>(route)

    override fun createScreenComponent(customActivityComponent: CustomActivityComponent,
                                       defaultActivityScreenModule: DefaultActivityScreenModule,
                                       intent: Intent): ScreenComponent<*> {
        @Suppress("DEPRECATION")
        return DaggerPushScreenConfigurator_PushScreenComponent.builder()
                .customActivityComponent(customActivityComponent)
                .defaultActivityScreenModule(defaultActivityScreenModule)
                .pushScreenModule(PushScreenModule(PushActivityRoute()))
                .build()
    }
}