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
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import ru.surfstudio.android.core.mvp.binding.rx.relation.Related
import ru.surfstudio.android.core.mvp.binding.rx.relation.mvp.PRESENTER
import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency
import ru.surfstudio.android.rx.extension.ActionSafe

/**
 * Презентер поддерживающий связывание модели и представления.
 * Работет в паре с [BindableRxView]
 */
abstract class BaseRxPresenter( //TODO разнести build-функции по интерфейсам
        basePresenterDependency: BasePresenterDependency
) : BasePresenter<BindableRxView>(basePresenterDependency), Related<PRESENTER> {

    val schedulersProvider = basePresenterDependency.schedulersProvider
    val errorHandler = basePresenterDependency.errorHandler

    override fun relationEntity() = PRESENTER

    override fun <T> subscribe(observable: Observable<T>,
                               onNext: Consumer<T>,
                               onError: (Throwable) -> Unit): Disposable =
            super.subscribe(observable, { onNext.accept(it) }, { onError(it) })

    /**
     * Build-функция, переводящая [Single] в поток из Schedulers.io()
     */
    protected fun <T> Single<T>.io(): Single<T> =
            subscribeOn(schedulersProvider.worker())

    /**
     * Build-функция, переводящая [Observable] в поток из Schedulers.io()
     */
    protected fun <T> Observable<T>.io(): Observable<T> =
            subscribeOn(schedulersProvider.worker())

    /**
     * Build-функция, переводящая [Observable] в поток из Schedulers.io()
     */
    protected fun <T> Maybe<T>.io(): Maybe<T> =
            subscribeOn(schedulersProvider.worker())

    /**
     * Build-функция, [Completable] в поток из Schedulers.io()
     */
    protected fun Completable.io(): Completable =
            subscribeOn(schedulersProvider.worker())

    /**
     * Build-функция, переводящая [Single] в поток из Schedulers.io()
     * и обрабатывающая возникающие ошибки с помощью [ErrorHandler] в главном потоке.
     */
    protected fun <T> Single<T>.ioHandleError(): Single<T> = this
            .io() // переводим цепочку в worker-thread
            .observeOn(schedulersProvider.main())   //ошибку обрабатываем в main
            .doOnError {
                errorHandler.handleError(it)
            }
            .observeOn(schedulersProvider.worker()) //дальнейшая работа происходит в worker

    /**
     * Build-функция, переводящая [Observable] в поток из Schedulers.io()
     * и обрабатывающая возникающие ошибки с помощью [ErrorHandler] в главном потоке.
     */
    protected fun <T> Observable<T>.ioHandleError(): Observable<T> = this
            .io() // переводим цепочку в worker-thread
            .observeOn(schedulersProvider.main())   //ошибку обрабатываем в main
            .doOnError {
                errorHandler.handleError(it)
            }
            .observeOn(schedulersProvider.worker()) //дальнейшая работа происходит в worker

    /**
     * Build-функция, переводящая [Maybe] в поток из Schedulers.io()
     * и обрабатывающая возникающие ошибки с помощью [ErrorHandler] в главном потоке.
     */
    protected fun <T> Maybe<T>.ioHandleError(): Maybe<T> = this
            .io() // переводим цепочку в worker-thread
            .observeOn(schedulersProvider.main())   //ошибку обрабатываем в main
            .doOnError {
                errorHandler.handleError(it)
            }
            .observeOn(schedulersProvider.worker()) //дальнейшая работа происходит в worker

    /**
     * Build-функция, переводящая [Completable] в поток из Schedulers.io()
     * и обрабатывающая возникающие ошибки с помощью [ErrorHandler] в главном потоке.
     */
    protected fun Completable.ioHandleError(): Completable = this
            .io() // переводим цепочку в worker-thread
            .observeOn(schedulersProvider.main())   //ошибку обрабатываем в main
            .doOnError {
                errorHandler.handleError(it)
            }
            .observeOn(schedulersProvider.worker()) //дальнейшая работа происходит в worker

    /**
     * Build-функция для [Single], которая при потере соединения с интернетом,
     * дожидается восстановления состояния, и выполняет действие.
     *
     * @param autoReloadAction - действие, выполняемое при восстановлении состояния
     */
    protected fun <T> Single<T>.autoReload(autoReloadAction: () -> Unit): Single<T> =
            doOnError(reloadErrorAction(autoReloadAction))

    /**
     * Build-функция для [Observable], которая при потере соединения с интернетом,
     * дожидается восстановления состояния, и выполняет действие.
     *
     * @param autoReloadAction - действие, выполняемое при восстановлении состояния
     */
    protected fun <T> Observable<T>.autoReload(autoReloadAction: () -> Unit): Observable<T> =
            doOnError(reloadErrorAction(autoReloadAction))

    /**
     * Build-функция для [Maybe], которая при потере соединения с интернетом,
     * дожидается восстановления состояния, и выполняет действие.
     *
     * @param autoReloadAction - действие, выполняемое при восстановлении состояния
     */
    protected fun <T> Maybe<T>.autoReload(autoReloadAction: () -> Unit): Maybe<T> =
            doOnError(reloadErrorAction(autoReloadAction))

    /**
     * Build-функция для [Completable], которая при потере соединения с интернетом,
     * дожидается восстановления состояния, и выполняет действие.
     *
     * @param autoReloadAction - действие, выполняемое при восстановлении состояния
     */
    protected fun Completable.autoReload(autoReloadAction: () -> Unit): Completable =
            doOnError(reloadErrorAction(autoReloadAction))

    //TODO добавить subscribeTakeLastFrozen(если возможно)
}