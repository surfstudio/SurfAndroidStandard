package ru.surfstudio.android.core.mvi.sample.ui.base.di

import dagger.Module
import dagger.Provides
import ru.surfstudio.android.core.mvi.impls.ui.middleware.BaseMiddlewareDependency
import ru.surfstudio.android.core.mvi.impls.ui.reactor.BaseReactorDependency
import ru.surfstudio.android.core.mvp.error.ErrorHandler
import ru.surfstudio.android.core.ui.state.ScreenState
import ru.surfstudio.android.dagger.scope.PerScreen
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProvider

/**
 * Модуль для базовых зависимостей mvi-экрана.
 * Следует прописать эти в базовом ScreenModule, если проект использует mvi
 */
@Module(includes = [EventHubModule::class, NavigationScreenModule::class])
class ReactScreenModule {

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

    @Provides
    @PerScreen
    fun provideBaseReactorDependency(errorHandler: ErrorHandler): BaseReactorDependency {
        return BaseReactorDependency(errorHandler)
    }
}