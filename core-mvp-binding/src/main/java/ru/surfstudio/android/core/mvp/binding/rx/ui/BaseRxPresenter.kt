/*
 * Copyright (c) 2019-present, SurfStudio LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.surfstudio.android.core.mvp.binding.rx.ui

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import ru.surfstudio.android.core.mvp.binding.rx.relation.Related
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.EmptyErrorException
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.LoadableState
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.PRESENTER
import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency

/**
 * Презентер поддерживающий связывание модели и представления.
 * Работет в паре с [BindableRxView]
 */
abstract class BaseRxPresenter(
        basePresenterDependency: BasePresenterDependency
) : BasePresenter<BindableRxView>(basePresenterDependency), Related<PRESENTER> {

    private val schedulersProvider = basePresenterDependency.schedulersProvider
    private val errorHandler = basePresenterDependency.errorHandler

    override fun relationEntity() = PRESENTER

    override fun <T> subscribe(observable: Observable<T>,
                               onNext: Consumer<T>,
                               onError: (Throwable) -> Unit): Disposable =
            super.subscribe(observable, { onNext.accept(it) }, { onError(it) })

    /**
     * Функция подписки на [Single] для [LoadableState].
     *
     * Автоматически поставляет значение, возможную ошибку и состояние загрузки на основе потока данных.
     */
    infix fun <T> Single<T>.bindTo(loadableState: LoadableState<T>) =
            subscribe(
                    this
                            .doOnSubscribe {
                                loadableState.isLoading.accept(true)
                            }
                            .doFinally {
                                loadableState.isLoading.accept(false)
                            },
                    {
                        loadableState.error.accept(EmptyErrorException())
                        loadableState.accept(it)
                    },
                    {
                        loadableState.error.accept(it)
                    }
            )


    /**
     * Функция подписки на [Observable] для [LoadableState].
     *
     * Автоматически поставляет значение, возможную ошибку и состояние загрузки на основе потока данных.
     */
    infix fun <T> Observable<T>.bindTo(loadableState: LoadableState<T>) =
            subscribe(
                    this
                            .doOnSubscribe {
                                loadableState.isLoading.accept(true)
                            }
                            .doFinally {
                                loadableState.isLoading.accept(false)
                            },
                    {
                        loadableState.error.accept(EmptyErrorException())
                        loadableState.accept(it)
                    },
                    {
                        loadableState.error.accept(it)
                    }
            )


    /**
     * Функция подписки на [Completable] для [LoadableState].
     *
     * Автоматически поставляет значение, возможную ошибку и состояние загрузки на основе потока данных.
     */
    infix fun Completable.bindTo(loadableState: LoadableState<Unit>) =
            subscribe(
                    this
                            .doOnSubscribe {
                                loadableState.isLoading.accept(true)
                            }
                            .doFinally {
                                loadableState.isLoading.accept(false)
                            },
                    {
                        loadableState.error.accept(EmptyErrorException())
                        loadableState.accept()
                    },
                    {
                        loadableState.error.accept(it)
                    }
            )


    /**
     * Build-функция, переводящая [Single] в поток из Schedulers.io()
     * и обрабатывающая возникающие ошибки с помощью [ErrorHandler] в главном потоке.
     */
    protected fun <T> Single<T>.handleError() = this
            .io() // переводим цепочку в worker-thread
            .observeOn(schedulersProvider.main())   //ошибку обрабатываем в main
            .doOnError {
                errorHandler.handleError(it)
            }
            .observeOn(schedulersProvider.worker()) //дальнейшая работа происходит в worker

    /**
     * Build-функция, переводящая [Single] в поток из Schedulers.io()
     * и обрабатывающая возникающие ошибки с помощью [ErrorHandler] в главном потоке.
     */
    protected fun <T> Observable<T>.handleError() = this
            .io() // переводим цепочку в worker-thread
            .observeOn(schedulersProvider.main())   //ошибку обрабатываем в main
            .doOnError {
                errorHandler.handleError(it)
            }
            .observeOn(schedulersProvider.worker()) //дальнейшая работа происходит в worker

    /**
     * Build-функция, переводящая [Single] в поток из Schedulers.io()
     * и обрабатывающая возникающие ошибки с помощью [ErrorHandler] в главном потоке.
     */
    protected fun Completable.handleError() = this
            .io() // переводим цепочку в worker-thread
            .observeOn(schedulersProvider.main())   //ошибку обрабатываем в main
            .doOnError {
                errorHandler.handleError(it)
            }
            .observeOn(schedulersProvider.worker()) //дальнейшая работа происходит в worker

    /**
     * Build-функция, переводящая [Single] в поток из Schedulers.io()
     */
    protected fun <T> Single<T>.io() =
            subscribeOn(schedulersProvider.worker())

    /**
     * Build-функция, переводящая [Observable] в поток из Schedulers.io()
     */
    protected fun <T> Observable<T>.io() =
            subscribeOn(schedulersProvider.worker())

    /**
     * Build-функция, [Completable] в поток из Schedulers.io()
     */
    protected fun Completable.io() =
            subscribeOn(schedulersProvider.worker())

}