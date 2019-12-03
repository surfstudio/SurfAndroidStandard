package ru.surfstudio.android.core.mvi.impls.ui.middleware

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.event.factory.EventFactory
import ru.surfstudio.android.core.mvi.impls.ui.middleware.dsl.BaseDslRxMiddleware
import ru.surfstudio.android.core.mvi.impls.ui.middleware.dsl.EventTransformerList
import ru.surfstudio.android.core.mvi.impls.ui.middleware.dsl.LifecycleMiddleware
import ru.surfstudio.android.core.mvi.ui.middleware.RxMiddleware
import ru.surfstudio.android.core.mvp.binding.rx.builders.RxBuilderHandleError
import ru.surfstudio.android.core.mvp.binding.rx.builders.RxBuilderIo
import ru.surfstudio.android.core.mvp.binding.rx.request.type.Request
import ru.surfstudio.android.core.mvp.error.ErrorHandler
import ru.surfstudio.android.logger.Logger
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProvider
import ru.surfstudio.android.rx.extension.toObservable

/**
 * Base [RxMiddleware] with DSL support and with builders for Rx requests.
 *
 * All screen middlewares should implement it.
 */
abstract class BaseMiddleware<T : Event>(
        baseMiddlewareDependency: BaseMiddlewareDependency
) : BaseDslRxMiddleware<T>,
        LifecycleMiddleware<T>,
        RxBuilderIo,
        RxBuilderHandleError {

    override val schedulersProvider: SchedulersProvider = baseMiddlewareDependency.schedulersProvider
    override val errorHandler: ErrorHandler = baseMiddlewareDependency.errorHandler

    protected fun <T> Observable<T>.ignoreErrors() = onErrorResumeNext { error: Throwable ->
        Logger.e(error) //логгируем ошибку, чтобы хотя бы знать, где она произошла
        Observable.empty()
    }

    protected fun <E> Observable<E>.ioHandleError() = io().handleError()

    protected fun <E> Single<E>.ioHandleError() = io().handleError()

    protected fun <E> Maybe<E>.ioHandleError() = io().handleError()

    protected fun Completable.ioHandleError() = io().handleError()

    /**
     * Трансформация [Observable]<[Request]<[T]>> Observable с событием с данными.
     *
     * Используется тогда, когда следить за состоянием загрузки/ошибки не требуется,
     * от запроса нужно только получение данных, и использование RequestEvent избыточно.
     */
    protected fun <D> Observable<Request<D>>.toDataEvent(eventFactory: EventFactory<D, out T>): Observable<out T> {
        return flatMap {
            if (it is Request.Success) {
                eventFactory(it.data).toObservable()
            } else {
                skip()
            }
        }
    }

    override fun provideTransformationList(eventStream: Observable<T>): EventTransformerList<T> = EventTransformerList(eventStream)
}