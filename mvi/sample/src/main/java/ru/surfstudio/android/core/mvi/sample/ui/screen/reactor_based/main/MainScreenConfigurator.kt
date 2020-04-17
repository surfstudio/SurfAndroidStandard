package ru.surfstudio.android.core.mvi.sample.ui.screen.reactor_based.main

import android.content.Intent
import dagger.Component
import dagger.Module
import dagger.Provides
import ru.surfstudio.android.core.mvi.impls.event.hub.ScreenEventHub
import ru.surfstudio.android.core.mvi.impls.event.hub.dependency.ScreenEventHubDependency
import ru.surfstudio.android.core.mvi.impls.ui.binder.ScreenBinder
import ru.surfstudio.android.core.mvi.impls.ui.binder.ScreenBinderDependency
import ru.surfstudio.android.core.mvi.impls.ui.dialog.EventHubDialogComponent
import ru.surfstudio.android.core.mvi.sample.ui.base.di.ReactScreenModule
import ru.surfstudio.android.core.mvp.configurator.BindableScreenComponent
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.sample.dagger.ui.base.configurator.DefaultActivityScreenConfigurator
import ru.surfstudio.android.sample.dagger.ui.base.dagger.activity.DefaultActivityComponent
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.DefaultActivityScreenModule

class MainScreenConfigurator(intent: Intent) : DefaultActivityScreenConfigurator(intent) {

    @PerScreen
    @Component(dependencies = [DefaultActivityComponent::class],
            modules = [DefaultActivityScreenModule::class, MainScreenModule::class, ReactScreenModule::class])
    internal interface MainScreenComponent
        : BindableScreenComponent<MainActivityView>,
            EventHubDialogComponent<MainEvent>

    @Module
    internal class MainScreenModule {

        @Provides
        @PerScreen
        fun provideEventHub(
                screenEventHubDependency: ScreenEventHubDependency
        ): ScreenEventHub<MainEvent> =
                ScreenEventHub(screenEventHubDependency, MainEvent::Lifecycle)

        @PerScreen
        @Provides
        fun provideBinder(
                screenBinderDependency: ScreenBinderDependency,
                eventHub: ScreenEventHub<MainEvent>,
                middleware: MainMiddleware
        ): Any = ScreenBinder(screenBinderDependency)
                .apply { bind(eventHub, middleware) }
    }

    override fun createScreenComponent(defaultActivityComponent: DefaultActivityComponent,
                                       defaultActivityScreenModule: DefaultActivityScreenModule,
                                       intent: Intent): ScreenComponent<*> {
        return DaggerMainScreenConfigurator_MainScreenComponent.builder()
                .defaultActivityComponent(defaultActivityComponent)
                .defaultActivityScreenModule(defaultActivityScreenModule)
                .mainScreenModule(MainScreenModule())
                .build()
    }
}
