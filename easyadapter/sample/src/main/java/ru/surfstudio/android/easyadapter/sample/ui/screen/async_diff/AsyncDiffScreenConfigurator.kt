package ru.surfstudio.android.easyadapter.sample.ui.screen.async_diff

import android.content.Intent
import dagger.Component
import dagger.Module
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.easyadapter.sample.ui.base.configurator.CustomActivityScreenConfigurator
import ru.surfstudio.android.easyadapter.sample.ui.base.dagger.activity.CustomActivityComponent
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.DefaultActivityScreenModule
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.DefaultCustomScreenModule

internal class AsyncDiffScreenConfigurator(intent: Intent) : CustomActivityScreenConfigurator(intent) {

    @PerScreen
    @Component(dependencies = [CustomActivityComponent::class],
            modules = [DefaultActivityScreenModule::class, AsyncDiffScreenModule::class])
    internal interface AsyncDiffScreenComponent : ScreenComponent<AsyncDiffActivityView>

    @Module
    internal class AsyncDiffScreenModule(route: AsyncDiffActivityRoute) : DefaultCustomScreenModule<AsyncDiffActivityRoute>(route)

    override fun createScreenComponent(
            customActivityComponent: CustomActivityComponent,
            defaultActivityScreenModule: DefaultActivityScreenModule,
            intent: Intent): ScreenComponent<*> {
        return DaggerAsyncDiffScreenConfigurator_AsyncDiffScreenComponent.builder()
                .customActivityComponent(customActivityComponent)
                .defaultActivityScreenModule(defaultActivityScreenModule)
                .asyncDiffScreenModule(AsyncDiffScreenModule(AsyncDiffActivityRoute()))
                .build()
    }
}