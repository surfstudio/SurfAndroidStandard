package ru.surfstudio.android.core.mvi.sample.ui.base.middleware

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.sample.ui.base.middleware.dependency.BaseMiddlewareDependency
import ru.surfstudio.android.core.mvi.ui.middleware.RxMiddleware
import ru.surfstudio.android.core.mvi.ui.middleware.dsl.BaseDslRxMiddleware
import ru.surfstudio.android.core.mvp.binding.rx.builders.RxBuilderHandleError
import ru.surfstudio.android.core.mvp.binding.rx.builders.RxBuilderIo
import ru.surfstudio.android.core.mvp.binding.rx.builders.UiBuilderFinish
import ru.surfstudio.android.core.mvp.error.ErrorHandler
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProvider

/**
 * Шаблонный базовый [RxMiddleware], который необходимо реализовать на проектах.
 */
abstract class BaseMiddleware<T : Event>(
        baseMiddlewareDependency: BaseMiddlewareDependency
) : BaseDslRxMiddleware<T>,
        RxBuilderIo,
        RxBuilderHandleError,
        UiBuilderFinish {

    override val activityNavigator: ActivityNavigator = baseMiddlewareDependency.activityNavigator
    override val schedulersProvider: SchedulersProvider = baseMiddlewareDependency.schedulersProvider
    override val errorHandler: ErrorHandler = baseMiddlewareDependency.errorHandler

    protected fun <T> Observable<T>.ignoreErrors() = onErrorResumeNext { error: Throwable ->
        Logger.e(error) //логгируем ошибку, чтобы хотя бы знать, где она произошла
        Observable.empty()
    }
}