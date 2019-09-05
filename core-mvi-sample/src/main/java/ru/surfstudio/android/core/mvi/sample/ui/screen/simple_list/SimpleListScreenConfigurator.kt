package ru.surfstudio.android.core.mvi.sample.ui.screen.simple_list


import android.content.Intent
import dagger.Component
import dagger.Module
import dagger.Provides
import ru.surfstudio.android.core.mvi.sample.ui.base.binder.BaseBinder
import ru.surfstudio.android.core.mvi.sample.ui.base.hub.BaseEventHub
import ru.surfstudio.android.core.mvi.sample.ui.base.middleware.dependency.BaseMiddlewareDependency
import ru.surfstudio.android.core.mvp.configurator.BindableScreenComponent
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.core.mvp.error.ErrorHandler
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.core.ui.state.ScreenState
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProvider
import ru.surfstudio.android.sample.dagger.ui.base.configurator.DefaultActivityScreenConfigurator
import ru.surfstudio.android.sample.dagger.ui.base.dagger.activity.DefaultActivityComponent
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.DefaultActivityScreenModule
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.DefaultCustomScreenModule

class SimpleListScreenConfigurator(intent: Intent) : DefaultActivityScreenConfigurator(intent) {

    @PerScreen
    @Component(dependencies = [DefaultActivityComponent::class],
            modules = [DefaultActivityScreenModule::class, SimpleListScreenModule::class])
    internal interface SimpleListScreenComponent
        : BindableScreenComponent<SimpleListActivityView>

    @Module
    internal class SimpleListScreenModule(route: SimpleListActivityRoute) : DefaultCustomScreenModule<SimpleListActivityRoute>(route) {

        @Provides
        @PerScreen
        fun provideBaseMiddlewareDependency(
                activityNavigator: ActivityNavigator,
                schedulersProvider: SchedulersProvider,
                errorHandler: ErrorHandler
        ) = BaseMiddlewareDependency(activityNavigator, schedulersProvider, errorHandler)

        @Provides
        @PerScreen
        fun provideEventHub(
                screenState: ScreenState,
                screenEventDelegateManager: ScreenEventDelegateManager
        ): BaseEventHub<SimpleListEvent> = BaseEventHub(
                screenState,
                screenEventDelegateManager
        ) { SimpleListEvent.SimpleListLifecycle(it) }

        @PerScreen
        @Provides
        fun provideBinder(
                basePresenterDependency: BasePresenterDependency,
                eventHub: BaseEventHub<SimpleListEvent>,
                middleware: SimpleListMiddleware,
                reactor: SimpleListReactor,
                stateHolder: SimpleListStateHolder
        ): Any = BaseBinder(basePresenterDependency)
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
