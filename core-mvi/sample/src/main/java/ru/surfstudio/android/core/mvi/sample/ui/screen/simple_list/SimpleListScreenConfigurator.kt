package ru.surfstudio.android.core.mvi.sample.ui.screen.simple_list


import android.content.Intent
import dagger.Component
import dagger.Module
import dagger.Provides
import ru.surfstudio.android.core.mvi.sample.ui.base.binder.ScreenBinder
import ru.surfstudio.android.core.mvi.sample.ui.base.di.ReactScreenModule
import ru.surfstudio.android.core.mvi.sample.ui.base.hub.ScreenEventHub
import ru.surfstudio.android.core.mvi.sample.ui.base.hub.dependency.ScreenEventHubDependency
import ru.surfstudio.android.core.mvi.sample.ui.base.middleware.dependency.BaseMiddlewareDependency
import ru.surfstudio.android.core.mvp.configurator.BindableScreenComponent
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.core.mvp.error.ErrorHandler
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProvider
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
                ScreenEventHub(screenEventHubDependency) { SimpleListEvent.SimpleListLifecycle(it) }

        @PerScreen
        @Provides
        fun provideBinder(
                basePresenterDependency: BasePresenterDependency,
                eventHub: ScreenEventHub<SimpleListEvent>,
                middleware: SimpleListMiddleware,
                reactor: SimpleListReactor,
                stateHolder: SimpleListStateHolder
        ): Any = ScreenBinder(basePresenterDependency)
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
