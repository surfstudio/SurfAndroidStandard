package ru.surfstudio.android.easyadapter.sample.ui.screen.async

import android.content.Intent
import dagger.Component
import dagger.Module
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.easyadapter.sample.ui.base.configurator.CustomActivityScreenConfigurator
import ru.surfstudio.android.easyadapter.sample.ui.base.dagger.activity.CustomActivityComponent
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.DefaultActivityScreenModule
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.DefaultCustomScreenModule

internal class AsyncInflateListScreenConfigurator(intent: Intent) : CustomActivityScreenConfigurator(intent) {
    @PerScreen
    @Component(dependencies = [CustomActivityComponent::class],
            modules = [DefaultActivityScreenModule::class, AsyncInflateListScreenModule::class])
    internal interface AsyncInflateListScreenComponent
        : ScreenComponent<AsyncInflateListActivityView>

    @Module
    internal class AsyncInflateListScreenModule(route: AsyncInflateListActivityRoute)
        : DefaultCustomScreenModule<AsyncInflateListActivityRoute>(route)

    override fun createScreenComponent(customActivityComponent: CustomActivityComponent,
                                       defaultActivityScreenModule: DefaultActivityScreenModule,
                                       intent: Intent): ScreenComponent<*> {
        return DaggerAsyncInflateListScreenConfigurator_AsyncInflateListScreenComponent.builder()
                .customActivityComponent(customActivityComponent)
                .defaultActivityScreenModule(defaultActivityScreenModule)
                .asyncInflateListScreenModule(AsyncInflateListScreenModule(AsyncInflateListActivityRoute()))
                .build()
    }
}