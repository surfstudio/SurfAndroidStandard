package ru.surfstudio.android.core.mvi.sample.ui.screen.kitties

import android.content.Intent
import dagger.Component
import dagger.Module
import dagger.Provides
import ru.surfstudio.android.core.mvi.impls.event.hub.ScreenEventHub
import ru.surfstudio.android.core.mvi.impls.event.hub.dependency.ScreenEventHubDependency
import ru.surfstudio.android.core.mvi.impls.ui.binder.ScreenBinder
import ru.surfstudio.android.core.mvi.impls.ui.binder.ScreenBinderDependency
import ru.surfstudio.android.core.mvi.sample.ui.base.di.ReactScreenModule
import ru.surfstudio.android.core.mvp.configurator.BindableScreenComponent
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.sample.dagger.ui.base.configurator.DefaultActivityScreenConfigurator
import ru.surfstudio.android.sample.dagger.ui.base.dagger.activity.DefaultActivityComponent
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.DefaultActivityScreenModule

internal class KittiesScreenConfigurator(intent: Intent?) : DefaultActivityScreenConfigurator(intent) {

    @PerScreen
    @Component(
            dependencies = [DefaultActivityComponent::class],
            modules = [DefaultActivityScreenModule::class, ReactScreenModule::class, KittiesScreenModule::class]
    )
    interface KittiesScreenComponent : BindableScreenComponent<KittiesActivityView>

    @Module
    class KittiesScreenModule {

        @Provides
        @PerScreen
        fun provideScreenEventHub(
                eventHubDependency: ScreenEventHubDependency
        ): ScreenEventHub<KittiesEvent> =
                ScreenEventHub(eventHubDependency, KittiesEvent::Lifecycle)

        @Provides
        @PerScreen
        fun provideScreenBinder(
                binderDependency: ScreenBinderDependency,
                eventHub: ScreenEventHub<KittiesEvent>,
                middleware: KittiesMiddleware,
                stateHolder: KittiesStateHolder,
                reducer: KittiesReducer
        ): Any = ScreenBinder(binderDependency)
                .apply { bind(eventHub, middleware, stateHolder, reducer) }

    }

    override fun createScreenComponent(
            parentActivityComponent: DefaultActivityComponent?,
            activityScreenModule: DefaultActivityScreenModule?,
            intent: Intent?
    ): ScreenComponent<*> = DaggerKittiesScreenConfigurator_KittiesScreenComponent.builder()
            .defaultActivityComponent(parentActivityComponent)
            .defaultActivityScreenModule(activityScreenModule)
            .kittiesScreenModule(KittiesScreenModule())
            .build()
}