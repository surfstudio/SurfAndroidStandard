package ru.surfstudio.android.core.mvi.sample.ui.screen.reducer_based.kitties.all

import android.content.Intent
import dagger.Component
import dagger.Module
import dagger.Provides
import ru.surfstudio.android.core.mvi.impls.event.hub.ScreenEventHub
import ru.surfstudio.android.core.mvi.impls.event.hub.dependency.ScreenEventHubDependency
import ru.surfstudio.android.core.mvi.impls.ui.binder.ScreenBinder
import ru.surfstudio.android.core.mvi.impls.ui.binder.ScreenBinderDependency
import ru.surfstudio.android.core.mvi.sample.ui.base.di.ReactScreenModule
import ru.surfstudio.android.core.mvi.sample.ui.screen.reducer_based.kitties.all.KittiesAllEvent.Lifecycle
import ru.surfstudio.android.core.mvp.configurator.BindableScreenComponent
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.sample.dagger.ui.base.configurator.DefaultActivityScreenConfigurator
import ru.surfstudio.android.sample.dagger.ui.base.dagger.activity.DefaultActivityComponent
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.DefaultActivityScreenModule

internal class KittiesAllScreenConfigurator(intent: Intent?) : DefaultActivityScreenConfigurator(intent) {

    @PerScreen
    @Component(
            dependencies = [DefaultActivityComponent::class],
            modules = [DefaultActivityScreenModule::class, ReactScreenModule::class, KittiesAllScreenModule::class]
    )
    interface KittiesAllScreenComponent : BindableScreenComponent<KittiesAllActivityView>

    @Module
    class KittiesAllScreenModule {

        @Provides
        @PerScreen
        fun provideEventHub(
                eventHubDependency: ScreenEventHubDependency
        ): ScreenEventHub<KittiesAllEvent> = ScreenEventHub(eventHubDependency, ::Lifecycle)

        @Provides
        @PerScreen
        fun provideBinder(
                binderDependency: ScreenBinderDependency,
                eventHub: ScreenEventHub<KittiesAllEvent>,
                middleware: KittiesAllMiddleware,
                stateHolder: KittiesAllStateHolder,
                reducer: KittiesAllReducer
        ): Any = ScreenBinder(binderDependency)
                .apply { bind(eventHub, middleware, stateHolder, reducer) }
    }

    override fun createScreenComponent(
            parentActivityComponent: DefaultActivityComponent?,
            activityScreenModule: DefaultActivityScreenModule?,
            intent: Intent?
    ): ScreenComponent<*> = DaggerKittiesAllScreenConfigurator_KittiesAllScreenComponent.builder()
            .defaultActivityComponent(parentActivityComponent)
            .defaultActivityScreenModule(activityScreenModule)
            .kittiesAllScreenModule(KittiesAllScreenModule())
            .build()
}