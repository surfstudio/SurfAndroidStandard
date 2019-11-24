package ru.surfstudio.android.core.mvi.impls.ui.middleware

import ru.surfstudio.android.core.mvp.error.ErrorHandler
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProvider

/**
 * Dependency for [BaseMiddleware]
 */
class BaseMiddlewareDependency(
        val schedulersProvider: SchedulersProvider,
        val errorHandler: ErrorHandler
)