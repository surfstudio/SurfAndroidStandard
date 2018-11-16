package ru.surfstudio.android.loadstate.sample.ui.screen.actions

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
 * Конфигуратор экрана todo
 */
class RendererWithActionsDemoScreenConfigurator(intent: Intent) : DefaultActivityScreenConfigurator(intent) {

    @PerScreen
    @Component(dependencies = [DefaultActivityComponent::class],
            modules = [DefaultActivityScreenModule::class, RendererWithActionsDemoScreenModule::class])
    internal interface RendererWithActionsDemoScreenComponent
        : ScreenComponent<RendererWithActionsDemoActivityView>

    @Module
    internal class RendererWithActionsDemoScreenModule(route: RendererWithActionsDemoActivityRoute)
        : DefaultCustomScreenModule<RendererWithActionsDemoActivityRoute>(route)

    override fun createScreenComponent(defaultActivityComponent: DefaultActivityComponent,
                                       defaultActivityScreenModule: DefaultActivityScreenModule,
                                       intent: Intent): ScreenComponent<*> {
        return DaggerRendererWithActionsDemoScreenConfigurator_RendererWithActionsDemoScreenComponent.builder()
                .defaultActivityComponent(defaultActivityComponent)
                .defaultActivityScreenModule(defaultActivityScreenModule)
                .rendererWithActionsDemoScreenModule(RendererWithActionsDemoScreenModule(RendererWithActionsDemoActivityRoute()))
                .build()
    }
}
