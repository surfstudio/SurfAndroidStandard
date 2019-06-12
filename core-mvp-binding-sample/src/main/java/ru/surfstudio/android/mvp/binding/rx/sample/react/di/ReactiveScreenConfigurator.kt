package ru.surfstudio.android.mvp.binding.rx.sample.react.di

import android.content.Intent
import dagger.Component
import dagger.Module
import dagger.Provides
import ru.surfstudio.android.core.mvp.binding.react.event.hub.EventHubImpl
import ru.surfstudio.android.core.mvp.binding.react.event.hub.RxEventHub
import ru.surfstudio.android.core.mvp.binding.react.ui.middleware.BaseMiddlewareDependency
import ru.surfstudio.android.core.mvp.configurator.BindableScreenComponent
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager
import ru.surfstudio.android.core.ui.state.ScreenState
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.mvp.binding.rx.sample.react.ReactiveActivityView
import ru.surfstudio.android.mvp.binding.rx.sample.react.ReactiveMiddleWare
import ru.surfstudio.android.mvp.binding.rx.sample.react.ReactiveScreenRoute
import ru.surfstudio.android.sample.dagger.ui.base.configurator.DefaultActivityScreenConfigurator
import ru.surfstudio.android.sample.dagger.ui.base.dagger.activity.DefaultActivityComponent
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.DefaultActivityScreenModule
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.DefaultCustomScreenModule

class ReactiveScreenConfigurator(intent: Intent) : DefaultActivityScreenConfigurator(intent) {

    @PerScreen
    @Component(dependencies = [DefaultActivityComponent::class],
            modules = [DefaultActivityScreenModule::class, ReactiveScreenModule::class, BaseReactScreenModule::class])
    internal interface ReactiveScreenComponent
        : BindableScreenComponent<ReactiveActivityView>

    @Module
    internal class ReactiveScreenModule(route: ReactiveScreenRoute) : DefaultCustomScreenModule<ReactiveScreenRoute>(route) {

        @PerScreen
        @Provides
        fun providePresenter(presenter: ReactiveMiddleWare): Any = presenter
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
