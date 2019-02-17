package ru.surfstudio.standard.f_debug.injector.ui.screen

import dagger.Module
import dagger.Provides
import ru.surfstudio.android.connection.ConnectionProvider
import ru.surfstudio.android.core.mvp.error.ErrorHandler
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.core.ui.state.ScreenState
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProvider

/**
 * Общий модуль для зависимостей Activity и Fragment
 */
@Module
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
                activityNavigator)
    }
}