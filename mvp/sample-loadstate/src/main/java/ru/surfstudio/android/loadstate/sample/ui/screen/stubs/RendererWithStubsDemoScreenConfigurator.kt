package ru.surfstudio.android.loadstate.sample.ui.screen.stubs

import android.content.Intent
import dagger.Component
import dagger.Module
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.sample.dagger.ui.base.configurator.DefaultActivityScreenConfigurator
import ru.surfstudio.android.sample.dagger.ui.base.dagger.activity.DefaultActivityComponent
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.DefaultActivityScreenModule
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.DefaultCustomScreenModule

/**
 * Конфигуратор экрана для демонстрации работы DefaultLoadStateRenderer с использованием заглушек (шиммеров)
 */
class RendererWithStubsDemoScreenConfigurator(intent: Intent) : DefaultActivityScreenConfigurator(intent) {
    @PerScreen
    @Component(dependencies = [DefaultActivityComponent::class],
            modules = [DefaultActivityScreenModule::class, RendererWithStubsDemoScreenModule::class])
    internal interface RendererWithStubsDemoScreenComponent
        : ScreenComponent<RendererWithStubsDemoActivityView>

    @Module
    internal class RendererWithStubsDemoScreenModule(route: RendererWithStubsDemoActivityRoute)
        : DefaultCustomScreenModule<RendererWithStubsDemoActivityRoute>(route)

    @Suppress("DEPRECATION")
    override fun createScreenComponent(defaultActivityComponent: DefaultActivityComponent,
                                       defaultActivityScreenModule: DefaultActivityScreenModule,
                                       intent: Intent): ScreenComponent<*> {
        return DaggerRendererWithStubsDemoScreenConfigurator_RendererWithStubsDemoScreenComponent.builder()
                .defaultActivityComponent(defaultActivityComponent)
                .defaultActivityScreenModule(defaultActivityScreenModule)
                .rendererWithStubsDemoScreenModule(RendererWithStubsDemoScreenModule(RendererWithStubsDemoActivityRoute()))
                .build()
    }
}
