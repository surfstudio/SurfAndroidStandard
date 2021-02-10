package ru.surfstudio.standard.ui.screen_modules

import dagger.Module
import dagger.Provides
import ru.surfstudio.android.connection.ConnectionProvider
import ru.surfstudio.android.core.mvi.impls.ui.middleware.BaseMiddlewareDependency
import ru.surfstudio.android.core.mvi.impls.ui.reactor.BaseReactorDependency
import ru.surfstudio.android.core.mvp.error.ErrorHandler
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.core.ui.state.ScreenState
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.navigation.provider.ActivityNavigationProvider
import ru.surfstudio.android.navigation.provider.FragmentProvider
import ru.surfstudio.android.navigation.scope.ScreenScopeNavigationProvider
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProvider
import ru.surfstudio.standard.ui.mvi.di.EventHubModule
import ru.surfstudio.standard.ui.mvi.di.NavigationMiddlewareModule

/**
 * Общий модуль для зависимостей Activity и Fragment
 */
@Module(
        includes = [
            NavigationScreenModule::class,
            NavigationMiddlewareModule::class,
            EventHubModule::class
        ]
)
abstract class ScreenModule {

    @PerScreen
    @Provides
    internal fun provideBaseDependency(
            schedulersProvider: SchedulersProvider,
            screenState: ScreenState,
            eventDelegateManager: ScreenEventDelegateManager,
            errorHandler: ErrorHandler,
            connectionProvider: ConnectionProvider,
            activityNavigator: ActivityNavigator
    ): BasePresenterDependency {
        return BasePresenterDependency(
                schedulersProvider,
                screenState,
                eventDelegateManager,
                errorHandler,
                connectionProvider,
                activityNavigator
        )
    }

    @PerScreen
    @Provides
    fun provideBaseMiddlewareDependency(
            schedulersProvider: SchedulersProvider,
            errorHandler: ErrorHandler,
            screenState: ScreenState
    ): BaseMiddlewareDependency = BaseMiddlewareDependency(
            schedulersProvider,
            errorHandler,
            screenState
    )

    @PerScreen
    @Provides
    fun provideBaseReactorDependency(
            errorHandler: ErrorHandler
    ): BaseReactorDependency = BaseReactorDependency(
            errorHandler
    )

    @PerScreen
    @Provides
    fun provideScreenScopeNavigationProvider(
            fragmentProvider: FragmentProvider,
            activityNavigationProvider: ActivityNavigationProvider
    ): ScreenScopeNavigationProvider {
        return ScreenScopeNavigationProvider(
                fragmentProvider,
                activityNavigationProvider
        )
    }
}