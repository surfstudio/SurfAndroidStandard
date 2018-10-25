package ru.surfstudio.standard.app_injector.ui.screen.configurator.activity

import android.content.Intent
import dagger.Component
import dagger.Module
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.standard.app_injector.ui.ActivityComponent
import ru.surfstudio.standard.app_injector.ui.configurator.ActivityScreenConfigurator
import ru.surfstudio.standard.app_injector.ui.screen.ActivityScreenModule
import ru.surfstudio.standard.app_injector.ui.screen.CustomScreenModule
import ru.surfstudio.standard.f_splash.SplashActivityView
import ru.surfstudio.standard.f_splash.SplashRoute

const val fbKey = "_fbSourceApplicationHasBeenSet"

/**
 * Конфигуратор стартового сплеш-экрана [SplashActivityView]
 */
class SplashScreenConfigurator(intent: Intent) : ActivityScreenConfigurator(intent) {

    @PerScreen
    @Component(dependencies = [ActivityComponent::class], modules = [ActivityScreenModule::class, SplashScreenModule::class])
    interface SplashScreenComponent : ScreenComponent<SplashActivityView>

    @Module
    class SplashScreenModule(route: SplashRoute) :
            CustomScreenModule<SplashRoute>(route)

    override fun createScreenComponent(activityComponent: ActivityComponent,
                                       activityScreenModule: ActivityScreenModule,
                                       intent: Intent): ScreenComponent<*> {
        @Suppress("DEPRECATION")
        return DaggerSplashScreenConfigurator_SplashScreenComponent.builder()
                .activityComponent(activityComponent)
                .activityScreenModule(activityScreenModule)
                .splashScreenModule(SplashScreenModule(SplashRoute()))
                .build()
    }
}