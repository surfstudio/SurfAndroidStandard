package ru.surfstudio.android.loadstate.sample.ui.screen.ordinary

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
 * Конфигуратор экрана для демонстрации работы DefaultLoadStateRenderer
 */
class DefaultRendererDemoScreenConfigurator(intent: Intent) : DefaultActivityScreenConfigurator(intent) {

    @PerScreen
    @Component(dependencies = [DefaultActivityComponent::class],
            modules = [DefaultActivityScreenModule::class, DefaultRendererDemoScreenModule::class])
    internal interface DefaultRendererDemoScreenComponent
        : ScreenComponent<DefaultRendererDemoActivityView>

    @Module
    internal class DefaultRendererDemoScreenModule(route: DefaultRendererDemoActivityRoute)
        : DefaultCustomScreenModule<DefaultRendererDemoActivityRoute>(route)

    @Suppress("DEPRECATION")
    override fun createScreenComponent(defaultActivityComponent: DefaultActivityComponent,
                                       defaultActivityScreenModule: DefaultActivityScreenModule,
                                       intent: Intent): ScreenComponent<*> {
        return DaggerDefaultRendererDemoScreenConfigurator_DefaultRendererDemoScreenComponent.builder()
                .defaultActivityComponent(defaultActivityComponent)
                .defaultActivityScreenModule(defaultActivityScreenModule)
                .defaultRendererDemoScreenModule(DefaultRendererDemoScreenModule(DefaultRendererDemoActivityRoute()))
                .build()
    }
}
