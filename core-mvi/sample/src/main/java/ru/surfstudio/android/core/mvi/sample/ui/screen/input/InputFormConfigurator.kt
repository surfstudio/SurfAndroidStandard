package ru.surfstudio.android.core.mvi.sample.ui.screen.input

import android.content.Intent
import dagger.Component
import dagger.Module
import dagger.Provides
import ru.surfstudio.android.core.mvi.sample.ui.base.binder.ScreenBinder
import ru.surfstudio.android.core.mvi.sample.ui.base.di.ReactScreenModule
import ru.surfstudio.android.core.mvi.sample.ui.base.hub.ScreenEventHub
import ru.surfstudio.android.core.mvi.sample.ui.base.hub.dependency.ScreenEventHubDependency
import ru.surfstudio.android.core.mvi.sample.ui.base.middleware.dependency.BaseNavMiddlewareDependency
import ru.surfstudio.android.core.mvp.configurator.BindableScreenComponent
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.core.mvp.error.ErrorHandler
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.core.ui.navigation.fragment.FragmentNavigator
import ru.surfstudio.android.core.ui.provider.ActivityProvider
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.mvp.dialog.navigation.navigator.DialogNavigator
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProvider
import ru.surfstudio.android.sample.dagger.ui.base.configurator.DefaultActivityScreenConfigurator
import ru.surfstudio.android.sample.dagger.ui.base.dagger.activity.DefaultActivityComponent
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.DefaultActivityScreenModule

class InputFormScreenConfigurator(intent: Intent) : DefaultActivityScreenConfigurator(intent) {

    @PerScreen
    @Component(dependencies = [DefaultActivityComponent::class],
            modules = [DefaultActivityScreenModule::class, InputFormScreenModule::class, ReactScreenModule::class])
    internal interface InputFormScreenComponent
        : BindableScreenComponent<InputFormActivityView>

    @Module
    internal class InputFormScreenModule {

        @Provides
        @PerScreen
        fun provideEventHub(
                screenEventHubDependency: ScreenEventHubDependency
        ): ScreenEventHub<InputFormEvent> =
                ScreenEventHub(screenEventHubDependency) { InputFormEvent.InputFormLifecycle(it) }

        @PerScreen
        @Provides
        fun provideBinder(
                basePresenterDependency: BasePresenterDependency,
                eventHub: ScreenEventHub<InputFormEvent>,
                middleware: InputFormMiddleware,
                reactor: InputFormReactor,
                stateHolder: InputFormStateHolder
        ): Any = ScreenBinder(basePresenterDependency)
                .apply { bind(eventHub, middleware, stateHolder, reactor) }
    }

    override fun createScreenComponent(defaultActivityComponent: DefaultActivityComponent,
                                       defaultActivityScreenModule: DefaultActivityScreenModule,
                                       intent: Intent): ScreenComponent<*> {
        return DaggerInputFormScreenConfigurator_InputFormScreenComponent.builder()
                .defaultActivityComponent(defaultActivityComponent)
                .defaultActivityScreenModule(defaultActivityScreenModule)
                .inputFormScreenModule(InputFormScreenModule())
                .build()
    }
}
