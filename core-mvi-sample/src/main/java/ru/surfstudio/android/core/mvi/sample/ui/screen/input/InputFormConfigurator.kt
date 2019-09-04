package ru.surfstudio.android.core.mvi.sample.ui.screen.input

import android.content.Intent
import dagger.Component
import dagger.Module
import dagger.Provides
import ru.surfstudio.android.core.mvi.sample.ui.base.binder.BaseBinder
import ru.surfstudio.android.core.mvi.sample.ui.base.hub.BaseEventHub
import ru.surfstudio.android.core.mvi.sample.ui.base.middleware.dependency.BaseNavMiddlewareDependency
import ru.surfstudio.android.core.mvp.configurator.BindableScreenComponent
import ru.surfstudio.android.core.mvp.configurator.ScreenComponent
import ru.surfstudio.android.core.mvp.error.ErrorHandler
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.core.ui.navigation.fragment.FragmentNavigator
import ru.surfstudio.android.core.ui.provider.ActivityProvider
import ru.surfstudio.android.core.ui.state.ScreenState
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.mvp.dialog.navigation.navigator.DialogNavigator
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProvider
import ru.surfstudio.android.sample.dagger.ui.base.configurator.DefaultActivityScreenConfigurator
import ru.surfstudio.android.sample.dagger.ui.base.dagger.activity.DefaultActivityComponent
import ru.surfstudio.android.sample.dagger.ui.base.dagger.screen.DefaultActivityScreenModule

class InputFormScreenConfigurator(intent: Intent) : DefaultActivityScreenConfigurator(intent) {

    @PerScreen
    @Component(dependencies = [DefaultActivityComponent::class],
            modules = [DefaultActivityScreenModule::class, InputFormScreenModule::class])
    internal interface InputFormScreenComponent
        : BindableScreenComponent<InputFormActivityView>

    @Module
    internal class InputFormScreenModule {
        @Provides
        @PerScreen
        fun provideFragmentNavigator(activityProvider: ActivityProvider): FragmentNavigator = FragmentNavigator(activityProvider)


        @Provides
        @PerScreen
        fun provideBaseMiddlewareDependency(
                activityNavigator: ActivityNavigator,
                schedulersProvider: SchedulersProvider,
                fragmentNavigator: FragmentNavigator,
                dialogNavigator: DialogNavigator,
                errorHandler: ErrorHandler
        ) = BaseNavMiddlewareDependency(activityNavigator, fragmentNavigator, dialogNavigator, schedulersProvider, errorHandler)

        @Provides
        @PerScreen
        fun provideEventHub(
                screenState: ScreenState,
                screenEventDelegateManager: ScreenEventDelegateManager
        ): BaseEventHub<InputFormEvent> = BaseEventHub(
                screenState,
                screenEventDelegateManager
        ) { InputFormEvent.InputFormLifecycle(it) }

        @PerScreen
        @Provides
        fun provideBinder(
                basePresenterDependency: BasePresenterDependency,
                eventHub: BaseEventHub<InputFormEvent>,
                middleware: InputFormMiddleware,
                reactor: InputFormReactor,
                stateHolder: InputFormStateHolder
        ): Any = BaseBinder(basePresenterDependency)
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
