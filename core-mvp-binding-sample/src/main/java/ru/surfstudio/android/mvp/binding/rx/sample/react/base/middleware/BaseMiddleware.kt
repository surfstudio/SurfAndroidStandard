package ru.surfstudio.android.mvp.binding.rx.sample.react.base.middleware

import ru.surfstudio.android.core.mvp.binding.react.event.Event
import ru.surfstudio.android.core.mvp.binding.react.builders.RxBuilderHandleError
import ru.surfstudio.android.core.mvp.binding.react.builders.RxBuilderIO
import ru.surfstudio.android.core.mvp.binding.react.builders.UiBuilderFinish
import ru.surfstudio.android.core.mvp.binding.react.ui.middleware.RxMiddleware
import ru.surfstudio.android.core.mvp.error.ErrorHandler
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProvider

/**
 * Шаблонный базовый [Middleware], который необходимо реализовать на проектах.
 */
abstract class BaseMiddleware<T : Event>(
        baseMiddlewareDependency: BaseMiddlewareDependency
) : RxMiddleware<T>, RxBuilderIO, RxBuilderHandleError, UiBuilderFinish {

    override val activityNavigator: ActivityNavigator = baseMiddlewareDependency.activityNavigator
    override val schedulersProvider: SchedulersProvider = baseMiddlewareDependency.schedulersProvider
    override val errorHandler: ErrorHandler = baseMiddlewareDependency.errorHandler
}