package ru.surfstudio.android.core.mvi.sample.ui.base.di

import dagger.Module
import dagger.Provides
import ru.surfstudio.android.core.mvi.sample.ui.base.hub.dependency.ScreenEventHubDependency
import ru.surfstudio.android.core.mvi.sample.ui.base.middleware.dependency.BaseMiddlewareDependency
import ru.surfstudio.android.core.mvi.sample.ui.base.middleware.dependency.BaseNavMiddlewareDependency
import ru.surfstudio.android.core.mvp.error.ErrorHandler
import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.core.ui.navigation.fragment.FragmentNavigator
import ru.surfstudio.android.core.ui.provider.ActivityProvider
import ru.surfstudio.android.core.ui.state.ScreenState
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.mvp.dialog.navigation.navigator.DialogNavigator
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProvider

/**
 * Модуль для базовых зависимостей mvi-экрана.
 * Следует прописать эти в базовом ScreenModule, если проект использует mvi
 */
@Module
class ReactScreenModule {

    @PerScreen
    @Provides
    fun provideBaseMiddlewareDependency(
            schedulersProvider: SchedulersProvider,
            errorHandler: ErrorHandler,
            activityNavigator: ActivityNavigator
    ): BaseMiddlewareDependency = BaseMiddlewareDependency(
            activityNavigator,
            schedulersProvider,
            errorHandler)

    @PerScreen
    @Provides
    fun provideBaseNavMiddlewareDependency(
            schedulersProvider: SchedulersProvider,
            errorHandler: ErrorHandler,
            activityNavigator: ActivityNavigator,
            fragmentNavigator: FragmentNavigator,
            dialogNavigator: DialogNavigator
    ): BaseNavMiddlewareDependency = BaseNavMiddlewareDependency(
            activityNavigator,
            fragmentNavigator,
            dialogNavigator,
            schedulersProvider,
            errorHandler)

    @PerScreen
    @Provides
    fun provideBaseEventHubDependency(
            screenState: ScreenState,
            screenEventDelegateManager: ScreenEventDelegateManager
    ): ScreenEventHubDependency = ScreenEventHubDependency(screenState, screenEventDelegateManager)

    @Provides
    @PerScreen
    fun provideFragmentNavigator(activityProvider: ActivityProvider): FragmentNavigator = FragmentNavigator(activityProvider)

}