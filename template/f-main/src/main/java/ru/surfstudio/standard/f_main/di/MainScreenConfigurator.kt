package ru.surfstudio.standard.f_main.di

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
import ru.surfstudio.standard.f_main.MainActivityView
import ru.surfstudio.standard.f_main.MainEvent
import ru.surfstudio.standard.f_main.MainMiddleware
import ru.surfstudio.standard.ui.activity.di.ActivityComponent
import ru.surfstudio.standard.ui.activity.di.ActivityScreenConfigurator
import ru.surfstudio.standard.ui.navigation.routes.MainActivityRoute
import ru.surfstudio.standard.ui.screen_modules.ActivityScreenModule
import ru.surfstudio.standard.ui.screen_modules.CustomScreenModule

/**
 * Конфигуратор главного экрана
 */
internal class MainScreenConfigurator(intent: Intent) : ActivityScreenConfigurator(intent) {

    @PerScreen
    @Component(dependencies = [ActivityComponent::class],
            modules = [ActivityScreenModule::class, MainScreenModule::class])
    interface MainScreenComponent : BindableScreenComponent<MainActivityView>

    @Module
    internal class MainScreenModule(route: MainActivityRoute) :
            CustomScreenModule<MainActivityRoute>(route) {

        @Provides
        @PerScreen
        fun provideEventHub(screenEventHubDependency: ScreenEventHubDependency) =
                ScreenEventHub<MainEvent>(screenEventHubDependency, MainEvent::Lifecycle)

        @Provides
        @PerScreen
        fun provideBinder(
                screenBinderDependency: ScreenBinderDependency,
                eventHub: ScreenEventHub<MainEvent>,
                middleware: MainMiddleware
        ): Any = ScreenBinder(screenBinderDependency).apply {
            bind(eventHub, middleware)
        }
    }

    @Suppress("DEPRECATION")
    override fun createScreenComponent(
            parentComponent: ActivityComponent,
            activityScreenModule: ActivityScreenModule,
            intent: Intent
    ): ScreenComponent<*> {
        return DaggerMainScreenConfigurator_MainScreenComponent.builder()
                .activityComponent(parentComponent)
                .activityScreenModule(activityScreenModule)
                .mainScreenModule(MainScreenModule(MainActivityRoute()))
                .build()
    }
}