package ru.surfstudio.android.app.migration.sample.ui.screen.splash

import android.content.Intent
import dagger.Component
import dagger.Module
import ru.surfstudio.android.app.migration.sample.ui.base.configurator.ActivityScreenConfigurator
import ru.surfstudio.android.app.migration.sample.ui.base.dagger.activity.ActivityComponent
import ru.surfstudio.android.app.migration.sample.ui.base.dagger.screen.ActivityScreenModule
import ru.surfstudio.android.app.migration.sample.ui.base.dagger.screen.CustomScreenModule
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.dagger.scope.PerScreen

/**
 * Конфигуратор для загрузочного экрана
 */
internal class SplashScreenConfigurator(intent: Intent) : ActivityScreenConfigurator(intent) {
    @PerScreen
    @Component(dependencies = [(ActivityComponent::class)],
            modules = [(ActivityScreenModule::class), (SplashScreenModule::class)])
    internal interface SplashScreenComponent : ScreenComponent<SplashActivityView>

    @Module
    internal class SplashScreenModule(route: SplashActivityRoute) : CustomScreenModule<SplashActivityRoute>(route)

    override fun createScreenComponent(activityComponent: ActivityComponent,
                                       activityScreenModule: ActivityScreenModule,
                                       intent: Intent): ScreenComponent<*> {
        return DaggerSplashScreenConfigurator_SplashScreenComponent.builder()
                .activityComponent(activityComponent)
                .activityScreenModule(activityScreenModule)
                .splashScreenModule(SplashScreenModule(SplashActivityRoute()))
                .build()
    }
}
