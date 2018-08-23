package ru.surfstudio.android.app.migration.sample.ui.screen.splash

import android.content.Intent
import dagger.Component
import dagger.Module
import ru.surfstudio.android.app.migration.sample.ui.base.configurator.ActivityScreenConfigurator
import ru.surfstudio.android.app.migration.sample.ui.base.dagger.activity.ActivityComponent
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.DefaultActivityScreenModule
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.DefaultCustomScreenModule

/**
 * Конфигуратор для загрузочного экрана
 */
internal class SplashScreenConfigurator(intent: Intent) : ActivityScreenConfigurator(intent) {
    @PerScreen
    @Component(dependencies = [(ActivityComponent::class)],
            modules = [(DefaultActivityScreenModule::class), (SplashScreenModule::class)])
    internal interface SplashScreenComponent
        : ScreenComponent<SplashActivityView>

    @Module
    internal class SplashScreenModule(route: SplashActivityRoute)
        : DefaultCustomScreenModule<SplashActivityRoute>(route)

    override fun createScreenComponent(activityComponent: ActivityComponent,
                                       defaultActivityScreenModule: DefaultActivityScreenModule,
                                       intent: Intent): ScreenComponent<*> {
        return DaggerSplashScreenConfigurator_SplashScreenComponent.builder()
                .activityComponent(activityComponent)
                .defaultActivityScreenModule(defaultActivityScreenModule)
                .splashScreenModule(SplashScreenModule(SplashActivityRoute()))
                .build()
    }
}
