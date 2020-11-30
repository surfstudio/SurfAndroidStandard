package ru.surfstudio.standard.ui.mvi.di

import dagger.Module
import dagger.Provides
import ru.surfstudio.android.core.mvi.impls.ui.freezer.LifecycleSubscriptionFreezer
import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.navigation.executor.NavigationCommandExecutor
import ru.surfstudio.standard.ui.mvi.navigation.AppNavigationMiddleware

/**
 * Модуль, который провайдит Middleware для навигации.
 */
@Module
class NavigationMiddlewareModule {

    @PerScreen
    @Provides
    internal fun provideLifecycleSubscriptionFreezer(
            eventDelegateManager: ScreenEventDelegateManager
    ): LifecycleSubscriptionFreezer {
        return LifecycleSubscriptionFreezer(eventDelegateManager)
    }

    @Provides
    @PerScreen
    fun provideNavigationMiddleware(
            navigationCommandExecutor: NavigationCommandExecutor,
            freezer: LifecycleSubscriptionFreezer
    ): AppNavigationMiddleware {
        return AppNavigationMiddleware(
                navigationCommandExecutor,
                freezer
        )
    }
}
