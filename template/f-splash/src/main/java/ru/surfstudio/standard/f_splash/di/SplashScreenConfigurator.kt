package ru.surfstudio.standard.f_splash.di

import android.content.Intent
import dagger.Component
import dagger.Module
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.standard.f_splash.SplashActivityView
import ru.surfstudio.standard.f_splash.SplashRoute
import ru.surfstudio.standard.ui.activity.di.ActivityComponent
import ru.surfstudio.standard.ui.activity.di.ActivityScreenConfigurator
import ru.surfstudio.standard.ui.screen.ActivityScreenModule
import ru.surfstudio.standard.ui.screen.CustomScreenModule

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