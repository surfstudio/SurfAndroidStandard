package ru.surfstudio.android.mvp.binding.rx.sample.react.di

import android.content.Intent
import dagger.Component
import dagger.Module
import dagger.Provides
import ru.surfstudio.android.core.mvp.binding.react.event.Event
import ru.surfstudio.android.core.mvp.binding.react.event.hub.EventHubImpl
import ru.surfstudio.android.core.mvp.binding.react.reactor.StateHolder
import ru.surfstudio.android.core.mvp.binding.react.ui.binder.Binder
import ru.surfstudio.android.core.mvp.binding.react.ui.binder.SingleBinder
import ru.surfstudio.android.core.mvp.configurator.BindableScreenComponent
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager
import ru.surfstudio.android.core.ui.state.ScreenState
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.mvp.binding.rx.sample.react.ReactiveActivityView
import ru.surfstudio.android.mvp.binding.rx.sample.react.ReactiveMiddleware
import ru.surfstudio.android.mvp.binding.rx.sample.react.ReactiveScreenRoute
import ru.surfstudio.android.mvp.binding.rx.sample.react.event.ListEvent
import ru.surfstudio.android.mvp.binding.rx.sample.react.reducer.ListReactor
import ru.surfstudio.android.mvp.binding.rx.sample.react.reducer.ListStateHolder
import ru.surfstudio.android.sample.dagger.ui.base.configurator.DefaultActivityScreenConfigurator
import ru.surfstudio.android.sample.dagger.ui.base.dagger.activity.DefaultActivityComponent
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.DefaultActivityScreenModule
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.DefaultCustomScreenModule

class ReactiveScreenConfigurator(intent: Intent) : DefaultActivityScreenConfigurator(intent) {

    @PerScreen
    @Component(dependencies = [DefaultActivityComponent::class],
            modules = [DefaultActivityScreenModule::class, ReactiveScreenModule::class])
    internal interface ReactiveScreenComponent
        : BindableScreenComponent<ReactiveActivityView>

    @Module
    internal class ReactiveScreenModule(route: ReactiveScreenRoute) : DefaultCustomScreenModule<ReactiveScreenRoute>(route) {

        @PerScreen
        @Provides
        fun providePresenter(binder: Binder): Any = binder

        @Provides
        @PerScreen
        fun provideEventHub(
                screenState: ScreenState,
                screenEventDelegateManager: ScreenEventDelegateManager
        ): EventHubImpl<ListEvent> = EventHubImpl(
                screenState,
                screenEventDelegateManager
        ) { ListEvent.Lifecycle(it) }

        @PerScreen
        @Provides
        fun provideBinder(
                basePresenterDependency: BasePresenterDependency,
                eventHub: EventHubImpl<ListEvent>,
                middleware: ReactiveMiddleware,
                reactor: ListReactor,
                stateHolder: ListStateHolder
        ): Binder = SingleBinder(eventHub, middleware, stateHolder, reactor, basePresenterDependency)
    }

    override fun createScreenComponent(defaultActivityComponent: DefaultActivityComponent,
                                       defaultActivityScreenModule: DefaultActivityScreenModule,
                                       intent: Intent): ScreenComponent<*> {
        return DaggerReactiveScreenConfigurator_ReactiveScreenComponent.builder()
                .defaultActivityComponent(defaultActivityComponent)
                .defaultActivityScreenModule(defaultActivityScreenModule)
                .reactiveScreenModule(ReactiveScreenModule(ReactiveScreenRoute()))
                .build()
    }
}
