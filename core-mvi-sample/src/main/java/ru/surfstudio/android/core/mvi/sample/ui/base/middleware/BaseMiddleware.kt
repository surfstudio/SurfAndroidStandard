package ru.surfstudio.android.core.mvi.sample.ui.base.middleware

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvp.binding.react.builders.RxBuilderHandleError
import ru.surfstudio.android.core.mvp.binding.react.builders.RxBuilderIO
import ru.surfstudio.android.core.mvp.binding.react.builders.UiBuilderFinish
import ru.surfstudio.android.core.mvp.error.ErrorHandler
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.core.mvi.ui.middleware.RxMiddleware
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

    override fun transform(eventStream: Observable<T>): Observable<out T> =
            eventStream.flatMap(this::flatMap)

    abstract fun flatMap(event: T): Observable<out T>
}