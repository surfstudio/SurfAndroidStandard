package ru.surfstudio.standard.ui.screen.splash

import android.app.Activity
import android.content.Intent

import dagger.Component
import dagger.Module
import ru.surfstudio.android.core.app.dagger.scope.PerScreen
import ru.surfstudio.android.core.ui.base.dagger.CoreActivityScreenModule
import ru.surfstudio.android.core.ui.base.dagger.CustomScreenModule
import ru.surfstudio.android.core.ui.base.screen.configurator.ScreenComponent
import ru.surfstudio.standard.app.dagger.ActivityComponent
import ru.surfstudio.standard.ui.base.dagger.ActivityScreenModule

/**
 * Конфигуратор для сплэш экрана, инкапсулирует всю логику работы с даггером.
 */
internal class SplashScreenConfigurator(activity: Activity, intent: Intent) : ActivityScreenConfigurator(activity, intent) {

    override fun getName(): String {
        return "splash"
    }

    @Module
    internal class SplashScreenModule(route: SplashRoute) : CustomScreenModule<SplashRoute>(route)

    override fun createScreenComponent(activityComponent: ActivityComponent,
                                       activityScreenModule: ActivityScreenModule,
                                       coreActivityScreenModule: CoreActivityScreenModule,
                                       intent: Intent): ScreenComponent<*> {
        return DaggerSplashScreenConfigurator_SplashScreenComponent.builder()
                .activityComponent(activityComponent)
                .activityScreenModule(activityScreenModule)
                .splashScreenModule(SplashScreenModule(SplashRoute()))
                .coreActivityScreenModule(coreActivityScreenModule)
                .build()
    }

    @PerScreen
    @Component(dependencies = [(ActivityComponent::class)], modules = [(ActivityScreenModule::class), (SplashScreenModule::class)])
    internal interface SplashScreenComponent : ScreenComponent<SplashActivityView>
}
