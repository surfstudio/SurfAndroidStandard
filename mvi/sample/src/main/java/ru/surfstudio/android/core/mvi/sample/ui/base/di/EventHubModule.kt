package ru.surfstudio.android.core.mvi.sample.ui.base.di

import dagger.Module
import dagger.Provides
import ru.surfstudio.android.core.mvi.impls.event.hub.dependency.ScreenEventHubDependency
import ru.surfstudio.android.core.mvi.impls.event.hub.logging.EventLogger
import ru.surfstudio.android.core.mvi.impls.ui.binder.ScreenBinderDependency
import ru.surfstudio.android.core.mvi.impls.ui.freezer.EmptySubscriptionFreezer
import ru.surfstudio.android.core.mvi.impls.ui.freezer.LifecycleSubscriptionFreezer
import ru.surfstudio.android.core.mvi.impls.ui.freezer.SubscriptionFreezer
import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager
import ru.surfstudio.android.core.ui.state.ScreenState
import ru.surfstudio.android.dagger.scope.PerScreen
import javax.inject.Named

/**
 * Модуль с зависимостями EventHub
 */
@Module
class EventHubModule {
    @PerScreen
    @Provides
    internal fun provideLifecycleSubscriptionFreezer(
            eventDelegateManager: ScreenEventDelegateManager
    ): LifecycleSubscriptionFreezer {
        return LifecycleSubscriptionFreezer(eventDelegateManager)
    }

    @PerScreen
    @Provides
    internal fun provideSubscriptionFreezer(
            lifecycleSubscriptionFreezer: LifecycleSubscriptionFreezer
    ): SubscriptionFreezer {
        return lifecycleSubscriptionFreezer
    }

    @PerScreen
    @Provides
    internal fun provideEmptySubscriptionFreezer(): EmptySubscriptionFreezer = EmptySubscriptionFreezer()

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
    @Named(EmptySubscriptionFreezer.TAG)
    internal fun provideEmptyScreenBinderDependency(
            eventDelegateManager: ScreenEventDelegateManager,
            subscriptionFreezer: EmptySubscriptionFreezer
    ): ScreenBinderDependency {
        return ScreenBinderDependency(eventDelegateManager, subscriptionFreezer)
    }

    @PerScreen
    @Provides
    fun provideBaseEventHubDependency(
            screenState: ScreenState,
            screenEventDelegateManager: ScreenEventDelegateManager
    ): ScreenEventHubDependency {
        return ScreenEventHubDependency(
                screenState,
                screenEventDelegateManager,
                EventLogger()
        )
    }

}