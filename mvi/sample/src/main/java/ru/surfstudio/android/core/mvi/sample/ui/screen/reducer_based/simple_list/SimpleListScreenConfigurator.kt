package ru.surfstudio.android.core.mvi.sample.ui.screen.reducer_based.simple_list


import android.content.Intent
import dagger.Component
import dagger.Module
import dagger.Provides
import ru.surfstudio.android.core.mvi.impls.event.hub.ScreenEventHub
import ru.surfstudio.android.core.mvi.impls.event.hub.dependency.ScreenEventHubDependency
import ru.surfstudio.android.core.mvi.impls.ui.binder.ScreenBinder
import ru.surfstudio.android.core.mvi.impls.ui.binder.ScreenBinderDependency
import ru.surfstudio.android.core.mvi.sample.ui.base.di.ReactScreenModule
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.State
import ru.surfstudio.android.core.mvp.configurator.BindableScreenComponent
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.sample.dagger.ui.base.configurator.DefaultActivityScreenConfigurator
import ru.surfstudio.android.sample.dagger.ui.base.dagger.activity.DefaultActivityComponent
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.DefaultActivityScreenModule
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.DefaultCustomScreenModule

class SimpleListScreenConfigurator(intent: Intent) : DefaultActivityScreenConfigurator(intent) {

    @PerScreen
    @Component(dependencies = [DefaultActivityComponent::class],
            modules = [DefaultActivityScreenModule::class, SimpleListScreenModule::class, ReactScreenModule::class])
    internal interface SimpleListScreenComponent
        : BindableScreenComponent<SimpleListActivityView>

    @Module
    internal class SimpleListScreenModule(route: SimpleListActivityRoute) : DefaultCustomScreenModule<SimpleListActivityRoute>(route) {

        @Provides
        @PerScreen
        fun provideEventHub(
                screenEventHubDependency: ScreenEventHubDependency
        ): ScreenEventHub<SimpleListEvent> =
                ScreenEventHub(screenEventHubDependency, SimpleListEvent::Lifecycle)

        @Provides
        @PerScreen
        fun provideState(): State<SimpleListModel> = State(SimpleListModel())

        @PerScreen
        @Provides
        fun provideBinder(
                screenBinderDependency: ScreenBinderDependency,
                eventHub: ScreenEventHub<SimpleListEvent>,
                middleware: SimpleListMiddleware,
                reactor: SimpleListReducer,
                stateHolder: State<SimpleListModel>
        ): Any = ScreenBinder(screenBinderDependency)
                .apply { bind(eventHub, middleware, stateHolder, reactor) }
    }

    override fun createScreenComponent(defaultActivityComponent: DefaultActivityComponent,
                                       defaultActivityScreenModule: DefaultActivityScreenModule,
                                       intent: Intent): ScreenComponent<*> {
        return DaggerSimpleListScreenConfigurator_SimpleListScreenComponent.builder()
                .defaultActivityComponent(defaultActivityComponent)
                .defaultActivityScreenModule(defaultActivityScreenModule)
                .simpleListScreenModule(SimpleListScreenModule(SimpleListActivityRoute()))
                .build()
    }
}
