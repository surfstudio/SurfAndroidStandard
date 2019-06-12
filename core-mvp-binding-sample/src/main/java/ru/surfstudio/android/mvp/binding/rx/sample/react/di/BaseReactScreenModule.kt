package ru.surfstudio.android.mvp.binding.rx.sample.react.di

import dagger.Module
import dagger.Provides
import ru.surfstudio.android.core.mvp.binding.react.event.hub.EventHubImpl
import ru.surfstudio.android.core.mvp.binding.react.event.hub.RxEventHub
import ru.surfstudio.android.core.mvp.binding.react.ui.middleware.BaseMiddlewareDependency
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager
import ru.surfstudio.android.core.ui.state.ScreenState
import ru.surfstudio.android.dagger.scope.PerScreen


/**
 * Базовый модуль экрана для реактивных компонентов.
 * Следует вынести все эти методы в Default*Module своего проекта.
 */
@Module
internal class BaseReactScreenModule {

    @Provides
    @PerScreen
    fun provideEventHub(
            screenState: ScreenState,
            screenEventDelegateManager: ScreenEventDelegateManager
    ) = EventHubImpl(screenState, screenEventDelegateManager)

    @Provides
    @PerScreen
    fun provideRxEventHub(eventHubImpl: EventHubImpl): RxEventHub = eventHubImpl

    @Provides
    @PerScreen
    fun provideBaseMiddlewareDependency(
            basePresenterDependency: BasePresenterDependency,
            rxEventHub: RxEventHub
    ) = BaseMiddlewareDependency(basePresenterDependency, rxEventHub)
}