package ru.surfstudio.android.easyadapter.sample.ui.screen.multitype

import android.content.Intent
import dagger.Component
import dagger.Module
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.easyadapter.sample.ui.base.configurator.CustomActivityScreenConfigurator
import ru.surfstudio.android.easyadapter.sample.ui.base.dagger.activity.CustomActivityComponent
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.DefaultActivityScreenModule
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.DefaultCustomScreenModule

internal class MultitypeListScreenConfigurator(intent: Intent) : CustomActivityScreenConfigurator(intent) {
    @PerScreen
    @Component(dependencies = [CustomActivityComponent::class],
            modules = [DefaultActivityScreenModule::class, MultitypeListScreenModule::class])
    internal interface MultitypeListScreenComponent
        : ScreenComponent<MultitypeListActivityView>

    @Module
    internal class MultitypeListScreenModule(route: MultitypeListActivityRoute)
        : DefaultCustomScreenModule<MultitypeListActivityRoute>(route)

    override fun createScreenComponent(customActivityComponent: CustomActivityComponent,
                                       defaultActivityScreenModule: DefaultActivityScreenModule,
                                       intent: Intent): ScreenComponent<*> {
        return DaggerMultitypeListScreenConfigurator_MultitypeListScreenComponent.builder()
                .customActivityComponent(customActivityComponent)
                .defaultActivityScreenModule(defaultActivityScreenModule)
                .multitypeListScreenModule(MultitypeListScreenModule(MultitypeListActivityRoute()))
                .build()
    }
}