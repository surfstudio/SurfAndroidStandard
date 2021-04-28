package ru.surfstudio.standard.f_splash.di

import android.content.Intent
import dagger.Component
import dagger.Module
import dagger.Provides
import ru.surfstudio.android.core.mvi.impls.event.hub.ScreenEventHub
import ru.surfstudio.android.core.mvi.impls.event.hub.dependency.ScreenEventHubDependency
import ru.surfstudio.android.core.mvi.impls.ui.binder.ScreenBinder
import ru.surfstudio.android.core.mvi.impls.ui.binder.ScreenBinderDependency
import ru.surfstudio.android.core.mvp.configurator.BindableScreenComponent
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.standard.f_splash.*
import ru.surfstudio.standard.ui.activity.di.ActivityComponent
import ru.surfstudio.standard.ui.activity.di.ActivityScreenConfigurator
import ru.surfstudio.standard.ui.screen_modules.ActivityScreenModule
import ru.surfstudio.standard.ui.screen_modules.CustomScreenModule

/**
 * Конфигуратор стартового сплеш-экрана [SplashActivityView]
 */
class SplashScreenConfigurator(intent: Intent) : ActivityScreenConfigurator(intent) {

    @PerScreen
    @Component(
            dependencies = [ActivityComponent::class],
            modules = [ActivityScreenModule::class, SplashScreenModule::class]
    )
    internal interface SplashScreenComponent : BindableScreenComponent<SplashActivityView>

    @Module
    internal class SplashScreenModule(route: SplashRoute) :
            CustomScreenModule<SplashRoute>(route) {

        @Provides
        @PerScreen
        fun provideEventHub(screenEventHubDependency: ScreenEventHubDependency): ScreenEventHub<SplashEvent> =
                ScreenEventHub(
                        screenEventHubDependency,
                        SplashEvent::Lifecycle
                )

        @Provides
        @PerScreen
        fun provideBinder(
                screenBinderDependency: ScreenBinderDependency,
                eventHub: ScreenEventHub<SplashEvent>,
                middleware: SplashMiddleware,
                stateHolder: SplashScreenStateHolder,
                reducer: SplashReducer
        ): Any = ScreenBinder(screenBinderDependency).apply {
            bind(eventHub, middleware, stateHolder, reducer)
        }
    }

    @Suppress("DEPRECATION")
    override fun createScreenComponent(
            parentComponent: ActivityComponent,
            activityScreenModule: ActivityScreenModule,
            intent: Intent
    ): ScreenComponent<*> {
        return DaggerSplashScreenConfigurator_SplashScreenComponent.builder()
                .activityComponent(parentComponent)
                .activityScreenModule(activityScreenModule)
                .splashScreenModule(SplashScreenModule(SplashRoute()))
                .build()
    }
}