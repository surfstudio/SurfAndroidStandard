package ru.surfstudio.standard.ui.base.dagger.screen

import dagger.Module
import dagger.Provides
import ru.surfstudio.android.connection.ConnectionProvider
import ru.surfstudio.android.core.mvp.error.ErrorHandler
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigatorForActivity
import ru.surfstudio.android.core.ui.navigation.fragment.FragmentNavigator
import ru.surfstudio.android.core.ui.provider.ActivityProvider
import ru.surfstudio.android.core.ui.state.ScreenState
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProvider

/**
 * Общий модуль для зависимостей Activity и Fragment
 */
@Module
abstract class ScreenModule {

    @Provides
    @PerScreen
    internal fun provideActivityNavigator(activityProvider: ActivityProvider,
                                          eventDelegateManager: ScreenEventDelegateManager): ActivityNavigator {
        return ActivityNavigatorForActivity(activityProvider, eventDelegateManager)
    }

    @Provides
    @PerScreen
    internal fun provideFragmentNavigator(activityProvider: ActivityProvider): FragmentNavigator {
        return FragmentNavigator(activityProvider)
    }


    @PerScreen
    @Provides
    internal fun provideBaseDependency(schedulersProvider: SchedulersProvider,
                                       screenState: ScreenState,
                                       eventDelegateManager: ScreenEventDelegateManager,
                                       errorHandler: ErrorHandler,
                                       connectionProvider: ConnectionProvider,
                                       activityNavigator: ActivityNavigator): BasePresenterDependency {
        return BasePresenterDependency(schedulersProvider,
                screenState,
                eventDelegateManager,
                errorHandler,
                connectionProvider,
                activityNavigator)
    }
}