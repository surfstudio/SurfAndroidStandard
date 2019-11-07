package ru.surfstudio.android.core.ui.sample.ui.screen.result

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
 * Конфигуратор активити главного экрана
 */
internal class ResultScreenConfigurator(intent: Intent) : DefaultActivityScreenConfigurator(intent) {
    @PerScreen
    @Component(dependencies = [DefaultActivityComponent::class],
            modules = [DefaultActivityScreenModule::class, ResultScreenModule::class])
    internal interface ResultScreenComponent
        : ScreenComponent<ResultActivityView>

    @Module
    internal class ResultScreenModule(route: ResultActivityRoute)
        : DefaultCustomScreenModule<ResultActivityRoute>(route)

    override fun createScreenComponent(defaultActivityComponent: DefaultActivityComponent,
                                       defaultActivityScreenModule: DefaultActivityScreenModule,
                                       intent: Intent): ScreenComponent<*> {
        return DaggerResultScreenConfigurator_ResultScreenComponent.builder()
                .defaultActivityComponent(defaultActivityComponent)
                .defaultActivityScreenModule(defaultActivityScreenModule)
                .resultScreenModule(ResultScreenModule(ResultActivityRoute()))
                .build()
    }
}
