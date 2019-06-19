package ru.surfstudio.android.mvp.binding.rx.sample.react.di

import android.content.Intent
import dagger.Component
import dagger.Module
import dagger.Provides
import ru.surfstudio.android.core.mvp.binding.react.event.hub.EventHubImpl
import ru.surfstudio.android.core.mvp.binding.react.ui.binder.SingleBinder
import ru.surfstudio.android.core.mvp.configurator.BindableScreenComponent
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager
import ru.surfstudio.android.core.ui.state.ScreenState
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.mvp.binding.rx.sample.react.ReactiveListActivityView
import ru.surfstudio.android.mvp.binding.rx.sample.react.ReactiveListMiddleware
import ru.surfstudio.android.mvp.binding.rx.sample.react.ReactiveListRoute
import ru.surfstudio.android.mvp.binding.rx.sample.react.event.ReactiveList
import ru.surfstudio.android.mvp.binding.rx.sample.react.reactor.ReactiveListReactor
import ru.surfstudio.android.mvp.binding.rx.sample.react.reactor.ReactiveListStateHolder
import ru.surfstudio.android.sample.dagger.ui.base.configurator.DefaultActivityScreenConfigurator
import ru.surfstudio.android.sample.dagger.ui.base.dagger.activity.DefaultActivityComponent
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.DefaultActivityScreenModule
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.DefaultCustomScreenModule

class ReactiveListScreenConfigurator(intent: Intent) : DefaultActivityScreenConfigurator(intent) {

    @PerScreen
    @Component(dependencies = [DefaultActivityComponent::class],
            modules = [DefaultActivityScreenModule::class, ReactiveListScreenModule::class])
    internal interface ReactiveListScreenComponent
        : BindableScreenComponent<ReactiveListActivityView>

    @Module
    internal class ReactiveListScreenModule(route: ReactiveListRoute) : DefaultCustomScreenModule<ReactiveListRoute>(route) {

        @Provides
        @PerScreen
        fun provideEventHub(
                screenState: ScreenState,
                screenEventDelegateManager: ScreenEventDelegateManager
        ): EventHubImpl<ReactiveList> = EventHubImpl(
                screenState,
                screenEventDelegateManager
        ) { ReactiveList.Lifecycle(it) }

        @PerScreen
        @Provides
        fun provideBinder(
                basePresenterDependency: BasePresenterDependency,
                eventHub: EventHubImpl<ReactiveList>,
                middleware: ReactiveListMiddleware,
                reactor: ReactiveListReactor,
                stateHolder: ReactiveListStateHolder
        ): Any = SingleBinder(eventHub, middleware, stateHolder, reactor, basePresenterDependency)
    }

    override fun createScreenComponent(defaultActivityComponent: DefaultActivityComponent,
                                       defaultActivityScreenModule: DefaultActivityScreenModule,
                                       intent: Intent): ScreenComponent<*> {
        return DaggerReactiveListScreenConfigurator_ReactiveListScreenComponent.builder()
                .defaultActivityComponent(defaultActivityComponent)
                .defaultActivityScreenModule(defaultActivityScreenModule)
                .reactiveListScreenModule(ReactiveListScreenModule(ReactiveListRoute()))
                .build()
    }
}
