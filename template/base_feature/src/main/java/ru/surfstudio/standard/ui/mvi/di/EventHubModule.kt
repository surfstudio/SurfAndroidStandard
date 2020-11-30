package ru.surfstudio.standard.ui.mvi.di

import dagger.Module
import dagger.Provides
import ru.surfstudio.android.core.mvi.impls.event.hub.dependency.ScreenEventHubDependency
import ru.surfstudio.android.core.mvi.impls.event.hub.logging.EventLogger
import ru.surfstudio.android.core.mvi.impls.ui.binder.ScreenBinderDependency
import ru.surfstudio.android.core.mvi.impls.ui.freezer.EmptySubscriptionFreezer
import ru.surfstudio.android.core.mvi.impls.ui.freezer.SubscriptionFreezer
import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager
import ru.surfstudio.android.core.ui.state.ScreenState
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.standard.ui.mvi.logger.DebugEventLogger

/**
 * Модуль с зависимостями EventHub
 */
@Module
class EventHubModule {

    @PerScreen
    @Provides
    internal fun provideEmptySubscriptionFreezer(): EmptySubscriptionFreezer =
            EmptySubscriptionFreezer()

    @PerScreen
    @Provides
    internal fun provideSubscriptionFreezer(
            emptySubscriptionFreezer: EmptySubscriptionFreezer
    ): SubscriptionFreezer {
        return emptySubscriptionFreezer
    }

    @Provides
    @PerScreen
    internal fun provideEventLogger(): EventLogger {
        return DebugEventLogger()
    }

    @PerScreen
    @Provides
    internal fun provideScreenBinderDependency(
            eventDelegateManager: ScreenEventDelegateManager,
            subscriptionFreezer: SubscriptionFreezer
    ): ScreenBinderDependency {
        return ScreenBinderDependency(eventDelegateManager, subscriptionFreezer)
    }

    @PerScreen
    @Provides
    fun provideBaseEventHubDependency(
            screenState: ScreenState,
            screenEventDelegateManager: ScreenEventDelegateManager,
            eventLogger: EventLogger
    ): ScreenEventHubDependency {
        return ScreenEventHubDependency(screenState, screenEventDelegateManager, eventLogger)
    }
}
