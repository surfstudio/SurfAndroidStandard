package ru.surfstudio.android.core.mvi.sample.ui.base.middleware

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.sample.ui.screen.list.EventTransformerList
import ru.surfstudio.android.core.mvp.binding.rx.builders.UiBuilderFinish
import ru.surfstudio.android.core.mvp.error.ErrorHandler
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.core.mvi.ui.middleware.RxMiddleware
import ru.surfstudio.android.core.mvp.binding.rx.builders.RxBuilderHandleError
import ru.surfstudio.android.core.mvp.binding.rx.builders.RxBuilderIo
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProvider

/**
 * Шаблонный базовый [RxMiddleware], который необходимо реализовать на проектах.
 */
abstract class BaseMiddleware<T : Event>(
        baseMiddlewareDependency: BaseMiddlewareDependency
) : RxMiddleware<T>, RxBuilderIo, RxBuilderHandleError, UiBuilderFinish {

    override val activityNavigator: ActivityNavigator = baseMiddlewareDependency.activityNavigator
    override val schedulersProvider: SchedulersProvider = baseMiddlewareDependency.schedulersProvider
    override val errorHandler: ErrorHandler = baseMiddlewareDependency.errorHandler

    protected fun merge(
            eventStream: Observable<T>,
            eventStreamBuilder: EventTransformerList<T>.() -> Unit
    ): Observable<out T> {
        val streamTransformers = EventTransformerList(eventStream)
        eventStreamBuilder.invoke(streamTransformers)
        return merge(*streamTransformers.toTypedArray())
    }

}