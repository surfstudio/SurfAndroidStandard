/*
  Copyright (c) 2018-present, SurfStudio LLC, Maxim Tuev.

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
package ru.surfstudio.android.core.mvp.presenter

import com.agna.ferro.rx.CompletableOperatorFreeze
import com.agna.ferro.rx.MaybeOperatorFreeze
import com.agna.ferro.rx.ObservableOperatorFreeze
import com.agna.ferro.rx.SingleOperatorFreeze
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.internal.observers.LambdaObserver
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.observers.DisposableMaybeObserver
import io.reactivex.observers.DisposableSingleObserver
import ru.surfstudio.android.connection.ConnectionProvider
import ru.surfstudio.android.core.mvp.error.ErrorHandler
import ru.surfstudio.android.core.mvp.view.CoreView
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.rx.extension.ConsumerSafe
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProvider

/**
 * базовый класс презентра для приложения
 * Подписки через все виды методов [.subscribe], [.subscribeWithoutFreezing],
 * [.subscribeIoHandleError] обрабатываются в главном потоке
 * При подписке с помощью методов [.subscribeIoHandleError] observable источника переводится в
 * background поток.
 * Кроме того [.subscribeIoHandleError] содержит стандартную обработку ошибок
 *
 *
 * Кроме того содержит метод [.finish] для закрытия текущего экрана, в дефолтной
 * имплементации закрывает активити
 *
 *
 * Имеет методы subscribe c постфиксом AutoReload, в которые следует передать лямбду, которая будет
 * вызвана при появлении интернета, если во время запроса интернета не было. Только последний запрос
 * подписанный с помощью этих методов будет с таким свойством
 * см [CorePresenter]
 *
 * @param <V>
</V> */
abstract class BasePresenter<V : CoreView>(basePresenterDependency: BasePresenterDependency) :
        CorePresenter<V>(basePresenterDependency.eventDelegateManager, basePresenterDependency.screenState) {

    private val onErrorStub: (Throwable) -> Unit = { _ -> }

    private val activityNavigator: ActivityNavigator
    private val schedulersProvider: SchedulersProvider
    private val connectionProvider: ConnectionProvider
    private var errorHandler: ErrorHandler? = null
    private var autoReloadDisposable: Disposable? = null

    init {
        this.schedulersProvider = basePresenterDependency.schedulersProvider
        this.activityNavigator = basePresenterDependency.activityNavigator
        this.connectionProvider = basePresenterDependency.connectionProvider
        this.errorHandler = basePresenterDependency.errorHandler
    }

    /**
     * закрывает текущую активити
     * если необходима другая логика закрытия экрана, следует переопределить этот метод
     */
    fun finish() {
        activityNavigator.finishCurrent()
    }

    /**
     * Устанавливает [ErrorHandler] вместо дефолтного
     */
    fun setErrorHandler(errorHandler: ErrorHandler) {
        this.errorHandler = errorHandler
    }

    /**
     * Стандартная обработка ошибки в презентере
     *
     * Переопределяем в случае если нужно специфичная обработка
     * @param e ошибка
     */
    protected fun handleError(e: Throwable) {
        errorHandler!!.handleError(e)
    }

    //SubscribeUi

    protected fun <T> Observable<T>.subscribeUi(operator: ObservableOperatorFreeze<T>,
                                                observer: LambdaObserver<T>): Disposable {
        return this.observeOn(schedulersProvider.main())
                .subscribeBy(operator, observer)
    }

    protected fun <T> Single<T>.subscribeUi(operator: SingleOperatorFreeze<T>,
                                            observer: DisposableSingleObserver<T>): Disposable {
        return this.observeOn(schedulersProvider.main())
                .subscribeBy(operator, observer)
    }

    protected fun Completable.subscribeUi(operator: CompletableOperatorFreeze,
                                          observer: DisposableCompletableObserver): Disposable {
        return this.observeOn(schedulersProvider.main())
                .subscribeBy(operator, observer)
    }

    protected fun <T> Maybe<T>.subscribeUi(operator: MaybeOperatorFreeze<T>,
                                           observer: DisposableMaybeObserver<T>): Disposable {
        return this.observeOn(schedulersProvider.main())
                .subscribeBy(operator, observer)
    }

    //End SubscribeUi

    //SubscribeWithoutFreezing
    protected fun <T> Observable<T>.subscribeWithoutFreezingUi(observer: LambdaObserver<T>): Disposable {
        return this.observeOn(schedulersProvider.main(), true)
                .subscribeWithoutFreezingBy(observer)
    }

    protected fun <T> Single<T>.subscribeWithoutFreezingUi(subscriber: DisposableSingleObserver<T>): Disposable {
        return this.observeOn(schedulersProvider.main())
                .subscribeWithoutFreezingBy(subscriber)
    }

    protected fun Completable.subscribeWithoutFreezingUi(subscriber: DisposableCompletableObserver): Disposable {
        return this.observeOn(schedulersProvider.main())
                .subscribeWithoutFreezingBy(subscriber)
    }

    protected fun <T> Maybe<T>.subscribeWithoutFreezingUi(subscriber: DisposableMaybeObserver<T>): Disposable {
        return this.observeOn(schedulersProvider.main())
                .subscribeWithoutFreezingBy(subscriber)
    }
    //End SubscribeWithoutFreezing

    //region subscribeIoHandleError

    protected fun <T> Observable<T>.subscribeByIoHandleError(onNext: (T) -> Unit): Disposable {
        return subscribeByIoHandleError(onNext, onErrorStub)
    }

    protected fun <T> Single<T>.subscribeByIoHandleError(onSuccess: (T) -> Unit): Disposable {
        return subscribeByIoHandleError(onSuccess, onErrorStub)
    }

    protected fun Completable.subscribeByIoHandleError(onComplete: () -> Unit): Disposable {
        return subscribeByIoHandleError(onComplete, onErrorStub)
    }

    protected fun <T> Maybe<T>.subscribeByIoHandleError(onSuccess: (T) -> Unit,
                                                        onComplete: () -> Unit): Disposable {
        return subscribeByIoHandleError(onSuccess, onComplete, onErrorStub)
    }

    protected fun <T> Observable<T>.subscribeByIoHandleError(onNext: (T) -> Unit,
                                                             onError: (Throwable) -> Unit): Disposable {
        return this.subscribeOn(schedulersProvider.worker())
                .subscribeBy(onNext, { e -> handleError(e, onError) })
    }

    protected fun <T> Observable<T>.subscribeByIoHandleError(onNext: (T) -> Unit,
                                                             onComplete: () -> Unit,
                                                             onError: (Throwable) -> Unit): Disposable {
        return this.subscribeOn(schedulersProvider.worker())
                .subscribeBy(onNext, onComplete, { e -> handleError(e, onError) })
    }

    protected fun <T> Single<T>.subscribeByIoHandleError(onSuccess: (T) -> Unit,
                                                         onError: (Throwable) -> Unit): Disposable {
        return this.subscribeOn(schedulersProvider.worker())
                .subscribeBy(onSuccess, { e -> handleError(e, onError) })
    }

    protected fun Completable.subscribeByIoHandleError(onComplete: () -> Unit,
                                                       onError: (Throwable) -> Unit): Disposable {
        return this.subscribeOn(schedulersProvider.worker())
                .subscribeBy(onComplete, { e -> handleError(e, onError) })
    }

    protected fun <T> Maybe<T>.subscribeByIoHandleError(onSuccess: (T) -> Unit,
                                                        onComplete: () -> Unit,
                                                        onError: (Throwable) -> Unit): Disposable {
        return this.subscribeOn(schedulersProvider.worker())
                .subscribeBy(onSuccess, onComplete, { e -> handleError(e, onError) })
    }

    //endregion

    //region subscribeIo
    protected fun <T> Observable<T>.subscribeByIo(onNext: (T) -> Unit,
                                                  onError: (Throwable) -> Unit): Disposable {
        return this.subscribeOn(schedulersProvider.worker())
                .subscribeBy(onNext, onError)
    }

    protected fun <T> Single<T>.subscribeByIo(onSuccess: (T) -> Unit,
                                              onError: (Throwable) -> Unit): Disposable {
        return this.subscribeOn(schedulersProvider.worker())
                .subscribeBy(onSuccess, onError)
    }

    protected fun Completable.subscribeByIo(onComplete: () -> Unit,
                                            onError: (Throwable) -> Unit): Disposable {
        return this.subscribeOn(schedulersProvider.worker())
                .subscribeBy(onComplete, onError)
    }

    protected fun <T> Observable<T>.subscribeByIo(onNext: (T) -> Unit,
                                                  onComplete: () -> Unit,
                                                  onError: (Throwable) -> Unit): Disposable {
        return this.subscribeOn(schedulersProvider.worker())
                .subscribeBy(onNext, onComplete, onError)
    }

    protected fun <T> Maybe<T>.subscribeByIo(onSuccess: (T) -> Unit,
                                             onComplete: () -> Unit,
                                             onError: (Throwable) -> Unit): Disposable {
        return this.subscribeOn(schedulersProvider.worker())
                .subscribeBy(onSuccess, onComplete, onError)
    }
    //endregion

    //region subscribeIoAutoReload

    /**
     * {@see subscribeIo}
     * автоматически вызовет autoReloadAction при появлении интернета если на момент выполнения
     * observable не было подключения к интернету
     */
    protected fun <T> Observable<T>.subscribeByIoAutoReload(autoReloadAction: () -> Unit,
                                                            onNext: (T) -> Unit,
                                                            onError: (Throwable) -> Unit): Disposable {
        return initializeAutoReload(this, autoReloadAction)
                .subscribeBy(onNext, onError)
    }

    protected fun <T> Observable<T>.subscribeByIoAutoReload(autoReloadAction: () -> Unit,
                                                            onNext: (T) -> Unit,
                                                            onComplete: () -> Unit,
                                                            onError: (Throwable) -> Unit): Disposable {
        return initializeAutoReload(this, autoReloadAction)
                .subscribeBy(onNext, onComplete, onError)
    }

    protected fun <T> Single<T>.subscribeByIoAutoReload(autoReloadAction: () -> Unit,
                                                        onSuccess: (T) -> Unit,
                                                        onError: (Throwable) -> Unit): Disposable {
        return initializeAutoReload(this, autoReloadAction)
                .subscribeBy(onSuccess, onError)
    }

    protected fun Completable.subscribeByIoAutoReload(autoReloadAction: () -> Unit,
                                                      onComplete: () -> Unit,
                                                      onError: (Throwable) -> Unit): Disposable {
        return initializeAutoReload(this, autoReloadAction)
                .subscribeBy(onComplete, onError)
    }

    protected fun <T> Maybe<T>.subscribeByIoAutoReload(autoReloadAction: () -> Unit,
                                                       onSuccess: (T) -> Unit,
                                                       onComplete: () -> Unit,
                                                       onError: (Throwable) -> Unit): Disposable {
        return initializeAutoReload(this, autoReloadAction)
                .subscribeBy(onSuccess, onComplete, onError)
    }
    //endregion

    protected fun <T> Observable<T>.subscribeByIoHandleErrorAutoReload(autoReloadAction: () -> Unit,
                                                                       onNext: (T) -> Unit,
                                                                       onError: (Throwable) -> Unit): Disposable {
        return initializeAutoReload(this, autoReloadAction).subscribeByIoHandleError(onNext, onError)
    }

    protected fun <T> Observable<T>.subscribeByIoHandleErrorAutoReload(autoReloadAction: () -> Unit,
                                                                       onNext: (T) -> Unit,
                                                                       onComplete: () -> Unit,
                                                                       onError: (Throwable) -> Unit): Disposable {
        return initializeAutoReload(this, autoReloadAction).subscribeByIoHandleError(onNext, onComplete, onError)
    }

    protected fun <T> Single<T>.subscribeByIoHandleErrorAutoReload(autoReloadAction: () -> Unit,
                                                                   onSuccess: (T) -> Unit,
                                                                   onError: (Throwable) -> Unit): Disposable {
        return initializeAutoReload(this, autoReloadAction).subscribeByIoHandleError(onSuccess, onError)
    }

    protected fun Completable.subscribeByIoHandleErrorAutoReload(autoReloadAction: () -> Unit,
                                                                 onComplete: () -> Unit,
                                                                 onError: (Throwable) -> Unit): Disposable {
        return initializeAutoReload(this, autoReloadAction).subscribeByIoHandleError(onComplete, onError)
    }

    protected fun <T> Maybe<T>.subscribeByIoHandleErrorAutoReload(autoReloadAction: () -> Unit,
                                                                  onSuccess: (T) -> Unit,
                                                                  onComplete: () -> Unit,
                                                                  onError: (Throwable) -> Unit): Disposable {
        return initializeAutoReload(this, autoReloadAction).subscribeByIoHandleError(onSuccess, onComplete, onError)
    }


    private fun handleError(e: Throwable, onError: (Throwable) -> Unit) {
        handleError(e)
        onError.invoke(e)
    }

    private fun <T> initializeAutoReload(observable: Observable<T>, reloadAction: () -> Unit): Observable<T> {
        return observable.doOnError(reloadErrorAction(reloadAction))
    }

    private fun <T> initializeAutoReload(single: Single<T>, reloadAction: () -> Unit): Single<T> {
        return single.doOnError(reloadErrorAction(reloadAction))
    }

    private fun initializeAutoReload(completable: Completable, reloadAction: () -> Unit): Completable {
        return completable.doOnError(reloadErrorAction(reloadAction))
    }

    private fun <T> initializeAutoReload(maybe: Maybe<T>, reloadAction: () -> Unit): Maybe<T> {
        return maybe.doOnError(reloadErrorAction(reloadAction))
    }

    private fun reloadErrorAction(reloadAction: () -> Unit): ConsumerSafe<Throwable> {
        return ConsumerSafe { _ ->
            cancelAutoReload()
            if (connectionProvider.isDisconnected) {
                autoReloadDisposable = connectionProvider.observeConnectionChanges()
                        .filter { connected -> connected }
                        .firstElement()
                        .toObservable().subscribeBy {
                            reloadAction.invoke()
                        }

            }
        }
    }

    private fun cancelAutoReload() {
        if (isDisposableActive(autoReloadDisposable)) {
            autoReloadDisposable!!.dispose()
        }
    }

    // TODO remove
    // Deprecated methods wil be removed

    //region subscribe ui
    @Deprecated("Use extension instead", ReplaceWith("observable.subscribeUi(operator, observer)"))
    protected fun <T> subscribe(observable: Observable<T>,
                                operator: ObservableOperatorFreeze<T>,
                                observer: LambdaObserver<T>): Disposable {
        return observable.subscribeUi(operator, observer)
    }


    @Deprecated("Use extension instead", ReplaceWith("single.subscribeUi(operator, observer)"))
    protected fun <T> subscribe(single: Single<T>,
                                operator: SingleOperatorFreeze<T>,
                                observer: DisposableSingleObserver<T>): Disposable {
        return single.subscribeUi(operator, observer)
    }

    @Deprecated("Use extension instead", ReplaceWith("completable.subscribeUi(disposables, operator, observer)"))
    protected fun subscribe(completable: Completable,
                            operator: CompletableOperatorFreeze,
                            observer: DisposableCompletableObserver): Disposable {
        return completable.subscribeUi(operator, observer)
    }

    @Deprecated("Use extension instead", ReplaceWith("maybe.subscribeUi(operator, observer)"))
    protected fun <T> subscribe(maybe: Maybe<T>,
                                operator: MaybeOperatorFreeze<T>,
                                observer: DisposableMaybeObserver<T>): Disposable {
        return maybe.subscribeUi(operator, observer)
    }
    //endregion

    //region subscribeWithoutFreezing
    @Deprecated("Use extension instead", ReplaceWith("observable.subscribeWithoutFreezingUi(observer)"))
    protected fun <T> subscribeWithoutFreezing(observable: Observable<T>,
                                               observer: LambdaObserver<T>): Disposable {
        return observable.subscribeWithoutFreezingUi(observer)
    }

    @Deprecated("Use extension instead", ReplaceWith("single.subscribeWithoutFreezingUi(subscriber)"))
    protected fun <T> subscribeWithoutFreezing(single: Single<T>,
                                               subscriber: DisposableSingleObserver<T>): Disposable {
        return single.subscribeWithoutFreezingUi(subscriber)
    }

    @Deprecated("Use extension instead", ReplaceWith("completable.subscribeWithoutFreezingUi(subscriber)"))
    protected fun subscribeWithoutFreezing(completable: Completable,
                                           subscriber: DisposableCompletableObserver): Disposable {
        return completable.subscribeWithoutFreezingUi(subscriber)
    }

    @Deprecated("Use extension instead", ReplaceWith("maybe.subscribeWithoutFreezingUi(subscriber)"))
    protected fun <T> subscribeWithoutFreezing(maybe: Maybe<T>,
                                               subscriber: DisposableMaybeObserver<T>): Disposable {
        return maybe.subscribeWithoutFreezingUi(subscriber)
    }

    //endregion

    //region subscribeIoHandleError

    /**
     * Работает также как [.subscribe], кроме того автоматически обрабатывает ошибки,
     * см [ErrorHandler] и переводит выполенения потока в фон
     */
    @Deprecated("Use extension instead", ReplaceWith("observable.subscribeByIoHandleError(onNext, null)"))
    protected fun <T> subscribeIoHandleError(observable: Observable<T>,
                                             onNext: (T) -> Unit): Disposable {
        return observable.subscribeByIoHandleError(onNext, onErrorStub)
    }

    @Deprecated("Use extension instead", ReplaceWith("single.subscribeByIoHandleError(onSuccess, null)"))
    protected fun <T> subscribeIoHandleError(single: Single<T>,
                                             onSuccess: (T) -> Unit): Disposable {
        return single.subscribeByIoHandleError(onSuccess, onErrorStub)
    }

    @Deprecated("Use extension instead", ReplaceWith("single.subscribeByIoHandleError(onSuccess, null)"))
    protected fun <T> subscribeIoHandleError(completable: Completable,
                                             onComplete: () -> Unit): Disposable {
        return completable.subscribeByIoHandleError(onComplete, onErrorStub)
    }

    @Deprecated("Use extension instead", ReplaceWith("maybe.subscribeByIoHandleError(onSuccess, onComplete, null)"))
    protected fun <T> subscribeIoHandleError(maybe: Maybe<T>,
                                             onSuccess: (T) -> Unit,
                                             onComplete: () -> Unit): Disposable {
        return maybe.subscribeByIoHandleError(onSuccess, onComplete, onErrorStub)
    }

    @Deprecated("Use extension instead")
    protected fun <T> subscribeIoHandleError(observable: Observable<T>,
                                             onNext: (T) -> Unit,
                                             onError: (Throwable) -> Unit): Disposable {
        return observable.subscribeByIoHandleError(onNext, { e -> handleError(e, onError) })
    }

    @Deprecated("Use extension instead")
    protected fun <T> subscribeIoHandleError(observable: Observable<T>,
                                             onNext: (T) -> Unit,
                                             onComplete: () -> Unit,
                                             onError: (Throwable) -> Unit): Disposable {
        return observable.subscribeByIoHandleError(onNext, onComplete, { e -> handleError(e, onError) })
    }

    @Deprecated("Use extension instead")
    protected fun <T> subscribeIoHandleError(single: Single<T>,
                                             onSuccess: (T) -> Unit,
                                             onError: (Throwable) -> Unit): Disposable {
        return single.subscribeByIoHandleError(onSuccess, { e -> handleError(e, onError) })
    }

    @Deprecated("Use extension instead")
    protected fun subscribeIoHandleError(completable: Completable,
                                         onComplete: () -> Unit,
                                         onError: (Throwable) -> Unit): Disposable {
        return completable.subscribeByIoHandleError(onComplete, { e -> handleError(e, onError) })
    }

    @Deprecated("Use extension instead")
    protected fun <T> subscribeIoHandleError(maybe: Maybe<T>,
                                             onSuccess: (T) -> Unit,
                                             onComplete: () -> Unit,
                                             onError: (Throwable) -> Unit): Disposable {
        return maybe.subscribeByIoHandleError(onSuccess, onComplete, { e -> handleError(e, onError) })
    }

    //endregion

    //region subscribeIo
    @Deprecated("Use extension instead", ReplaceWith("observable.subscribeByIo(onNext, onError)"))
    protected fun <T> subscribeIo(observable: Observable<T>,
                                  onNext: (T) -> Unit,
                                  onError: (Throwable) -> Unit): Disposable {
        return observable.subscribeByIo(onNext, onError)
    }

    @Deprecated("Use extension instead", ReplaceWith("single.subscribeByIo(onSuccess, onError)"))
    protected fun <T> subscribeIo(single: Single<T>,
                                  onSuccess: (T) -> Unit,
                                  onError: (Throwable) -> Unit): Disposable {
        return single.subscribeByIo(onSuccess, onError)
    }

    @Deprecated("Use extension instead", ReplaceWith("completable.subscribeByIo(onComplete, onError)"))
    protected fun subscribeIo(completable: Completable,
                              onComplete: () -> Unit,
                              onError: (Throwable) -> Unit): Disposable {
        return completable.subscribeByIo(onComplete, onError)
    }

    @Deprecated("Use extension instead", ReplaceWith("observable.subscribeByIo(onNext, onComplete, onError)"))
    protected fun <T> subscribeIo(observable: Observable<T>,
                                  onNext: (T) -> Unit,
                                  onComplete: () -> Unit,
                                  onError: (Throwable) -> Unit): Disposable {
        return observable.subscribeByIo(onNext, onComplete, onError)
    }

    @Deprecated("Use extension instead", ReplaceWith("maybe.subscribeByIo(onSuccess, onComplete, onError)"))
    protected fun <T> subscribeIo(maybe: Maybe<T>,
                                  onSuccess: (T) -> Unit,
                                  onComplete: () -> Unit,
                                  onError: (Throwable) -> Unit): Disposable {
        return maybe.subscribeByIo(onSuccess, onComplete, onError)
    }
    //endregion

    //region subscribeIoAutoReload

    /**
     * {@see subscribeIo}
     * автоматически вызовет autoReloadAction при появлении интернета если на момент выполнения
     * observable не было подключения к интернету
     */
    @Deprecated("Use extension instead", ReplaceWith("observable.subscribeByIoAutoReload(autoReloadAction, onNext, onError)"))
    protected fun <T> subscribeIoAutoReload(observable: Observable<T>,
                                            autoReloadAction: () -> Unit,
                                            onNext: (T) -> Unit,
                                            onError: (Throwable) -> Unit): Disposable {
        return observable.subscribeByIoAutoReload(autoReloadAction, onNext, onError)
    }

    @Deprecated("Use extension instead", ReplaceWith("observable.subscribeByIoAutoReload(autoReloadAction, onNext, onComplete, onError)"))
    protected fun <T> subscribeIoAutoReload(observable: Observable<T>,
                                            autoReloadAction: () -> Unit,
                                            onNext: (T) -> Unit,
                                            onComplete: () -> Unit,
                                            onError: (Throwable) -> Unit): Disposable {
        return observable.subscribeByIoAutoReload(autoReloadAction, onNext, onComplete, onError)
    }

    @Deprecated("Use extension instead", ReplaceWith("single.subscribeByIoAutoReload(autoReloadAction, onSuccess, onError)"))
    protected fun <T> subscribeIoAutoReload(single: Single<T>,
                                            autoReloadAction: () -> Unit,
                                            onSuccess: (T) -> Unit,
                                            onError: (Throwable) -> Unit): Disposable {
        return single.subscribeByIoAutoReload(autoReloadAction, onSuccess, onError)
    }

    @Deprecated("Use extension instead", ReplaceWith("completable.subscribeByIoAutoReload(autoReloadAction, onComplete, onError)"))
    protected fun subscribeIoAutoReload(completable: Completable,
                                        autoReloadAction: () -> Unit,
                                        onComplete: () -> Unit,
                                        onError: (Throwable) -> Unit): Disposable {
        return completable.subscribeByIoAutoReload(autoReloadAction, onComplete, onError)
    }

    @Deprecated("Use extension instead", ReplaceWith("maybe.subscribeByIoAutoReload(autoReloadAction, onSuccess, onComplete, onError)"))
    protected fun <T> subscribeIoAutoReload(maybe: Maybe<T>,
                                            autoReloadAction: () -> Unit,
                                            onSuccess: (T) -> Unit,
                                            onComplete: () -> Unit,
                                            onError: (Throwable) -> Unit): Disposable {
        return maybe.subscribeByIoAutoReload(autoReloadAction, onSuccess, onComplete, onError)
    }
    //endregion

    //region subscribeIoHandleErrorAutoReload

    /**
     * {@see subscribeIoAutoReload} кроме того автоматически обрабатывает ошибки
     */
    @Deprecated("Use extension instead")
    protected fun <T> subscribeIoHandleErrorAutoReload(observable: Observable<T>,
                                                       autoReloadAction: () -> Unit,
                                                       onNext: (T) -> Unit,
                                                       onError: (Throwable) -> Unit): Disposable {
        return initializeAutoReload(observable, autoReloadAction).subscribeByIoHandleError(onNext, onError)
    }

    @Deprecated("Use extension instead")
    protected fun <T> subscribeIoHandleErrorAutoReload(observable: Observable<T>,
                                                       autoReloadAction: () -> Unit,
                                                       onNext: (T) -> Unit,
                                                       onComplete: () -> Unit,
                                                       onError: (Throwable) -> Unit): Disposable {
        return initializeAutoReload(observable, autoReloadAction).subscribeByIoHandleError(onNext, onComplete, onError)
    }

    @Deprecated("Use extension instead")
    protected fun <T> subscribeIoHandleErrorAutoReload(single: Single<T>,
                                                       autoReloadAction: () -> Unit,
                                                       onSuccess: (T) -> Unit,
                                                       onError: (Throwable) -> Unit): Disposable {
        return initializeAutoReload(single, autoReloadAction).subscribeByIoHandleError(onSuccess, onError)
    }

    @Deprecated("Use extension instead")
    protected fun subscribeIoHandleErrorAutoReload(completable: Completable,
                                                   autoReloadAction: () -> Unit,
                                                   onComplete: () -> Unit,
                                                   onError: (Throwable) -> Unit): Disposable {
        return initializeAutoReload(completable, autoReloadAction).subscribeByIoHandleError(onComplete, onError)
    }

    @Deprecated("Use extension instead")
    protected fun <T> subscribeIoHandleErrorAutoReload(maybe: Maybe<T>,
                                                       autoReloadAction: () -> Unit,
                                                       onSuccess: (T) -> Unit,
                                                       onComplete: () -> Unit,
                                                       onError: (Throwable) -> Unit): Disposable {
        return initializeAutoReload(maybe, autoReloadAction).subscribeByIoHandleError(onSuccess, onComplete, onError)
    }

    //endregion
}
