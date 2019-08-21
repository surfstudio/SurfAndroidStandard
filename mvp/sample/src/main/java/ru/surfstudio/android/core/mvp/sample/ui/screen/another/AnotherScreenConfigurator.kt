package ru.surfstudio.android.core.mvp.sample.ui.screen.another

import android.content.Intent
import dagger.Component
import dagger.Module
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.sample.dagger.ui.base.configurator.DefaultActivityScreenConfigurator
import ru.surfstudio.android.sample.dagger.ui.base.dagger.activity.DefaultActivityComponent
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.DefaultActivityScreenModule
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.DefaultCustomScreenModule

internal class AnotherScreenConfigurator(intent: Intent) : DefaultActivityScreenConfigurator(intent) {
    @PerScreen
    @Component(dependencies = [DefaultActivityComponent::class],
            modules = [DefaultActivityScreenModule::class, AnotherScreenModule::class])
    internal interface AnotherScreenComponent
        : ScreenComponent<AnotherActivityView>

    @Module
    internal class AnotherScreenModule(route: AnotherActivityRoute)
        : DefaultCustomScreenModule<AnotherActivityRoute>(route)

    override fun createScreenComponent(defaultActivityComponent: DefaultActivityComponent,
                                       defaultActivityScreenModule: DefaultActivityScreenModule,
                                       intent: Intent): ScreenComponent<*> {
        return DaggerAnotherScreenConfigurator_AnotherScreenComponent.builder()
                .defaultActivityComponent(defaultActivityComponent)
                .defaultActivityScreenModule(defaultActivityScreenModule)
                .anotherScreenModule(AnotherScreenModule(AnotherActivityRoute()))
                .build()
    }
}