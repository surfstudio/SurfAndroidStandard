package ru.surfstudio.android.core.mvi.sample.ui.base.middleware

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.sample.ui.base.middleware.experimental.OpenScreenMiddleware
import ru.surfstudio.android.core.mvi.ui.dsl.EventTransformerList
import ru.surfstudio.android.core.mvp.binding.rx.builders.UiBuilderFinish
import ru.surfstudio.android.core.mvp.error.ErrorHandler
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.core.mvi.ui.middleware.RxMiddleware
import ru.surfstudio.android.core.mvi.ui.middleware.builders.FlatMapMiddleware
import ru.surfstudio.android.core.mvp.binding.rx.builders.RxBuilderHandleError
import ru.surfstudio.android.core.mvp.binding.rx.builders.RxBuilderIo
import ru.surfstudio.android.core.ui.event.result.SupportOnActivityResultRoute
import ru.surfstudio.android.core.ui.navigation.fragment.FragmentNavigator
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.android.mvp.dialog.navigation.navigator.DialogNavigator
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProvider

/**
 * Шаблонный базовый [RxMiddleware], который необходимо реализовать на проектах.
 */
abstract class BaseMiddleware<T : Event>(
        baseMiddlewareDependency: BaseMiddlewareDependency
) : RxMiddleware<T>,
        RxBuilderIo,
        RxBuilderHandleError,
        UiBuilderFinish,
        FlatMapMiddleware<T>,
        OpenScreenMiddleware<T> {

    override val activityNavigator: ActivityNavigator = baseMiddlewareDependency.activityNavigator
    override val schedulersProvider: SchedulersProvider = baseMiddlewareDependency.schedulersProvider
    override val errorHandler: ErrorHandler = baseMiddlewareDependency.errorHandler
    override val dialogNavigator: DialogNavigator = baseMiddlewareDependency.dialogNavigator
    override val fragmentNavigator: FragmentNavigator = baseMiddlewareDependency.fragmentNavigator

    override val observableRoutes: MutableList<Class<out SupportOnActivityResultRoute<*>>> = mutableListOf()

    protected fun transformations(
            eventStream: Observable<T>,
            eventStreamBuilder: EventTransformerList<T>.() -> Unit
    ): Observable<out T> {
        val streamTransformers = EventTransformerList(eventStream)
        eventStreamBuilder.invoke(streamTransformers)
        return merge(*streamTransformers.toTypedArray())
    }

    protected fun <T> Observable<T>.ignoreErrors() = onErrorResumeNext { error: Throwable ->
        Logger.e(error) //логгируем ошибку, чтобы хотя бы знать, где она произошла
        Observable.empty()
    }


}