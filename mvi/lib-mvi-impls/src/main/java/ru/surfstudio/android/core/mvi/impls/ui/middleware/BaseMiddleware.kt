/*
  Copyright (c) 2020, SurfStudio LLC.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
*/
package ru.surfstudio.android.core.mvi.impls.ui.middleware

import io.reactivex.Observable
import ru.surfstudio.android.core.mvi.event.Event
import ru.surfstudio.android.core.mvi.event.factory.EventFactory
import ru.surfstudio.android.core.mvi.event.lifecycle.LifecycleEvent
import ru.surfstudio.android.core.mvi.impls.ui.middleware.dsl.BaseDslRxMiddleware
import ru.surfstudio.android.core.mvi.impls.ui.middleware.dsl.EventTransformerList
import ru.surfstudio.android.core.mvi.impls.ui.middleware.dsl.LifecycleMiddleware
import ru.surfstudio.android.core.mvi.ui.middleware.RxMiddleware
import ru.surfstudio.android.core.mvi.util.filterIsInstance
import ru.surfstudio.android.core.mvp.binding.rx.builders.RxBuilderHandleError
import ru.surfstudio.android.core.mvp.binding.rx.builders.RxBuilderIo
import ru.surfstudio.android.core.mvp.binding.rx.request.Request
import ru.surfstudio.android.core.mvp.error.ErrorHandler
import ru.surfstudio.android.core.ui.state.LifecycleStage
import ru.surfstudio.android.core.ui.state.ScreenState
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
    protected val screenState: ScreenState = baseMiddlewareDependency.screenState

    /**
     * Заморозка событий до момента наступления resumed-состояния экрана
     *
     * @param eventStream список событий экрана
     * @param onResumeObservableCreator - лямбда, лениво создающая observable,
     * который будет выполняться в при достижении resumed-состояния.
     *
     * @return [Observable]<[T]>
     */
    protected fun <T : Event> freezeUntilResume(
            eventStream: Observable<T>,
            onResumeObservableCreator: () -> Observable<out T>
    ): Observable<out T> {
        val currentStage = screenState.lifecycleStage
        return if (currentStage == LifecycleStage.RESUMED) {
            onResumeObservableCreator()
        } else {
            eventStream
                    .filterIsInstance<LifecycleEvent>()
                    .takeUntil { it.stage == LifecycleStage.RESUMED }
                    .takeLast(1)
                    .flatMap { onResumeObservableCreator() }
        }
    }


    protected fun <T> Observable<T>.ignoreErrors() = onErrorResumeNext { error: Throwable ->
        Logger.e(error) //логгируем ошибку, чтобы хотя бы знать, где она произошла
        Observable.empty()
    }

    /**
     * Трансформация [Observable]<[Request]<[T]>> Observable с событием с данными.
     *
     * Используется тогда, когда следить за состоянием загрузки/ошибки не требуется,
     * от запроса нужно только получение данных, и использование RequestEvent избыточно.
     */
    protected fun <D> Observable<Request<D>>.toDataEvent(eventFactory: EventFactory<D, out T>): Observable<out T> {
        return flatMap {
            if (it.isSuccess) {
                eventFactory(it.getData()).toObservable()
            } else {
                skip()
            }
        }
    }

    override fun provideTransformationList(eventStream: Observable<T>): EventTransformerList<T> =
            EventTransformerList(eventStream).apply {
                add(createReactorTriggerTransformation(eventStream))
            }

    /**
     * Creates transformation which does nothing, but consumes all events from eventStream.
     *
     * It serves to handle specific case:
     * if [Middleware] doesn't contain any transformations, [Reactor.react] method won't be triggered,
     * even if stream contains events from UI, which should be reacted directly.
     *
     * As a workaround, we can add transformation,
     * which will consume all input events but wont produce any output.
     * This transformation will work like a trigger to [Reactor.react] method.
     */
    private fun createReactorTriggerTransformation(eventStream: Observable<T>) =
            eventStream.filter { it !is T }
}