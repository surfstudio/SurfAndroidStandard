package ru.surfstudio.android.core.mvi.impls.ui.middleware

import ru.surfstudio.android.core.mvp.error.ErrorHandler
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProvider

/**
 * Шаблонная зависимость для [BaseMiddleware], которую нужно провайдить на проектах.
 */
class BaseMiddlewareDependency(
        val activityNavigator: ActivityNavigator,
        val schedulersProvider: SchedulersProvider,
        val errorHandler: ErrorHandler
)