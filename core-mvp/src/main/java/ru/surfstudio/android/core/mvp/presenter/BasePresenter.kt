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
import io.reactivex.internal.functions.Functions
import io.reactivex.internal.observers.LambdaObserver
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.observers.DisposableMaybeObserver
import io.reactivex.observers.DisposableSingleObserver
import ru.surfstudio.android.connection.ConnectionProvider
import ru.surfstudio.android.core.mvp.error.ErrorHandler
import ru.surfstudio.android.core.mvp.view.CoreView
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator
import ru.surfstudio.android.rx.extension.ActionSafe
import ru.surfstudio.android.rx.extension.BiFunctionSafe
import ru.surfstudio.android.rx.extension.ConsumerSafe
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProvider

/**
 * базовый класс презентра для приложения
 * Подписки через все виды методов [.subscribeBy], [.subscribeWithoutFreezingBy],
 * [.subscribeIoHandleErrorBy] обрабатываются в главном потоке
 * При подписке с помощью методов [.subscribeIoHandleErrorBy] observable источника переводится в
 * background поток.
 * Кроме того [.subscribeIoHandleErrorBy] содержит стандартную обработку ошибок
 *
 *
 * Кроме того содержит метод [.finish] для закрытия текущего экрана, в дефолтной
 * имплементации закрывает активити
 *
 *
 * Имеет методы subscribe c постфиксом AutoReload, в которые следует передать лямбду, которая будет
 * вызвана при появлении интернета, если во время запроса интернета не было. Все такие запросы будут выполнены после появления интернета.
 * см [CorePresenter]
 *
 * Существует поддержка вызова методов [.subscribeBy] из java кода. Такие методы вместо лямбда-выражений
 * принимают функциональный интерфейс.
 *
 * @param <V>
</V> */
abstract class BasePresenter<V : CoreView>(basePresenterDependency: BasePresenterDependency) :
        CorePresenter<V>(basePresenterDependency.eventDelegateManager, basePresenterDependency.screenState) {

    private val activityNavigator: ActivityNavigator
    private val schedulersProvider: SchedulersProvider
    private val connectionProvider: ConnectionProvider
    private var errorHandler: ErrorHandler
    private var autoReloadDisposables: HashMap<Int, Disposable> = hashMapOf()

    init {
        this.schedulersProvider = basePresenterDependency.schedulersProvider
        this.activityNavigator = basePresenterDependency.activityNavigator
        this.connectionProvider = basePresenterDependency.connectionProvider
        this.errorHandler = basePresenterDependency.errorHandler
    }

    override fun onDestroy() {
        super.onDestroy()
        for (disposable: Disposable in autoReloadDisposables.values) {
            if (isDisposableActive(disposable)) {
                disposable.dispose()
            }
        }
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
        errorHandler.handleError(e)
    }

    //region subscribeBy

    override fun <T> subscribeBy(observable: Observable<T>, operator: ObservableOperatorFreeze<T>, observer: LambdaObserver<T>): Disposable {
        return super.subscribeBy(observable.observeOn(schedulersProvider.main(), true), operator, observer)
    }

    override fun <T> subscribeBy(single: Single<T>, operator: SingleOperatorFreeze<T>, observer: DisposableSingleObserver<T>): Disposable {
        return super.subscribeBy(single.observeOn(schedulersProvider.main()), operator, observer)
    }

    override fun <T> subscribeBy(maybe: Maybe<T>, operator: MaybeOperatorFreeze<T>, observer: DisposableMaybeObserver<T>): Disposable {
        return super.subscribeBy(maybe.observeOn(schedulersProvider.main()), operator, observer)
    }

    override fun subscribeBy(completable: Completable, operator: CompletableOperatorFreeze, observer: DisposableCompletableObserver): Disposable {
        return super.subscribeBy(completable.observeOn(schedulersProvider.main()), operator, observer)
    }

    //endregion

    //region subscribe

    protected fun <T> Observable<T>.subscribeBy(onNext: (T) -> Unit): Disposable {

        return this.subscribeBy(onNext, onCompleteStub, onErrorNotImplemented)
    }

    protected fun <T> Observable<T>.subscribeBy(onNext: (T) -> Unit,
                                                onError: (Throwable) -> Unit): Disposable {

        return this.subscribeBy(onNext, onCompleteStub, onError)
    }

    //endregion

    //region subscribeUi

    protected fun <T> Observable<T>.subscribeUiBy(onNext: (T) -> Unit): Disposable {

        return this.subscribeUiBy(onNext, onCompleteStub, onErrorNotImplemented)
    }

    protected fun <T> Observable<T>.subscribeUiBy(onNext: (T) -> Unit,
                                                  onError: (Throwable) -> Unit): Disposable {

        return this.subscribeUiBy(onNext, onCompleteStub, onError)
    }

    //endregion

    //region subscribeIoHandleErrorBy

    protected fun <T> Observable<T>.subscribeIoHandleErrorBy(onNext: (T) -> Unit): Disposable {
        return subscribeIoHandleErrorBy(onNext, onErrorStub)
    }

    protected fun <T> Single<T>.subscribeIoHandleErrorBy(onSuccess: (T) -> Unit): Disposable {
        return subscribeIoHandleErrorBy(onSuccess, onErrorStub)
    }

    protected fun Completable.subscribeIoHandleErrorBy(onComplete: () -> Unit): Disposable {
        return subscribeIoHandleErrorBy(onComplete, onErrorStub)
    }

    protected fun <T> Maybe<T>.subscribeIoHandleErrorBy(onSuccess: (T) -> Unit,
                                                        onComplete: () -> Unit): Disposable {
        return subscribeIoHandleErrorBy(onSuccess, onComplete, onErrorStub)
    }

    protected fun <T> Observable<T>.subscribeIoHandleErrorBy(onNext: (T) -> Unit,
                                                             onError: (Throwable) -> Unit): Disposable {
        return this.subscribeOn(schedulersProvider.worker())
                .subscribeBy(onNext, { e -> handleError(e, onError) })
    }

    protected fun <T> Observable<T>.subscribeIoHandleErrorBy(onNext: (T) -> Unit,
                                                             onComplete: () -> Unit,
                                                             onError: (Throwable) -> Unit): Disposable {
        return this.subscribeOn(schedulersProvider.worker())
                .subscribeBy(onNext, onComplete, { e -> handleError(e, onError) })
    }

    protected fun <T> Single<T>.subscribeIoHandleErrorBy(onSuccess: (T) -> Unit,
                                                         onError: (Throwable) -> Unit): Disposable {
        return this.subscribeOn(schedulersProvider.worker())
                .subscribeBy(onSuccess, { e -> handleError(e, onError) })
    }

    protected fun Completable.subscribeIoHandleErrorBy(onComplete: () -> Unit,
                                                       onError: (Throwable) -> Unit): Disposable {
        return this.subscribeOn(schedulersProvider.worker())
                .subscribeBy(onComplete, { e -> handleError(e, onError) })
    }

    protected fun <T> Maybe<T>.subscribeIoHandleErrorBy(onSuccess: (T) -> Unit,
                                                        onComplete: () -> Unit,
                                                        onError: (Throwable) -> Unit): Disposable {
        return this.subscribeOn(schedulersProvider.worker())
                .subscribeBy(onSuccess, onComplete, { e -> handleError(e, onError) })
    }

    //endregion

    //region subscribeIoBy
    protected fun <T> Observable<T>.subscribeIoBy(onNext: (T) -> Unit,
                                                  onError: (Throwable) -> Unit): Disposable {
        return this.subscribeOn(schedulersProvider.worker())
                .subscribeBy(onNext, onError)
    }

    protected fun <T> Single<T>.subscribeIoBy(onSuccess: (T) -> Unit,
                                              onError: (Throwable) -> Unit): Disposable {
        return this.subscribeOn(schedulersProvider.worker())
                .subscribeBy(onSuccess, onError)
    }

    protected fun Completable.subscribeIoBy(onComplete: () -> Unit,
                                            onError: (Throwable) -> Unit): Disposable {
        return this.subscribeOn(schedulersProvider.worker())
                .subscribeBy(onComplete, onError)
    }

    protected fun <T> Observable<T>.subscribeIoBy(onNext: (T) -> Unit,
                                                  onComplete: () -> Unit,
                                                  onError: (Throwable) -> Unit): Disposable {
        return this.subscribeOn(schedulersProvider.worker())
                .subscribeBy(onNext, onComplete, onError)
    }

    protected fun <T> Maybe<T>.subscribeIoBy(onSuccess: (T) -> Unit,
                                             onComplete: () -> Unit,
                                             onError: (Throwable) -> Unit): Disposable {
        return this.subscribeOn(schedulersProvider.worker())
                .subscribeBy(onSuccess, onComplete, onError)
    }
    //endregion

    //region subscribeIoAutoReloadBy

    /**
     * {@see subscribeIo}
     * автоматически вызовет autoReloadAction при появлении интернета если на момент выполнения
     * observable не было подключения к интернету
     */
    protected fun <T> Observable<T>.subscribeIoAutoReloadBy(autoReloadAction: () -> Unit,
                                                            onNext: (T) -> Unit,
                                                            onError: (Throwable) -> Unit): Disposable {
        return initializeAutoReload(this, autoReloadAction)
                .subscribeIoBy(onNext, onError)
    }

    protected fun <T> Observable<T>.subscribeIoAutoReloadBy(autoReloadAction: () -> Unit,
                                                            onNext: (T) -> Unit,
                                                            onComplete: () -> Unit,
                                                            onError: (Throwable) -> Unit): Disposable {
        return initializeAutoReload(this, autoReloadAction)
                .subscribeIoBy(onNext, onComplete, onError)
    }

    protected fun <T> Single<T>.subscribeIoAutoReloadBy(autoReloadAction: () -> Unit,
                                                        onSuccess: (T) -> Unit,
                                                        onError: (Throwable) -> Unit): Disposable {
        return initializeAutoReload(this, autoReloadAction)
                .subscribeIoBy(onSuccess, onError)
    }

    protected fun Completable.subscribeIoAutoReloadBy(autoReloadAction: () -> Unit,
                                                      onComplete: () -> Unit,
                                                      onError: (Throwable) -> Unit): Disposable {
        return initializeAutoReload(this, autoReloadAction)
                .subscribeIoBy(onComplete, onError)
    }

    protected fun <T> Maybe<T>.subscribeIoAutoReloadBy(autoReloadAction: () -> Unit,
                                                       onSuccess: (T) -> Unit,
                                                       onComplete: () -> Unit,
                                                       onError: (Throwable) -> Unit): Disposable {
        return initializeAutoReload(this, autoReloadAction)
                .subscribeIoBy(onSuccess, onComplete, onError)
    }
    //endregion

    //region subscribeIoHandleErrorAutoReloadBy

    protected fun <T> Observable<T>.subscribeIoHandleErrorAutoReloadBy(autoReloadAction: () -> Unit,
                                                                       onNext: (T) -> Unit,
                                                                       onError: (Throwable) -> Unit): Disposable {
        return initializeAutoReload(this, autoReloadAction)
                .subscribeIoHandleErrorBy(onNext, onError)
    }

    protected fun <T> Observable<T>.subscribeIoHandleErrorAutoReloadBy(autoReloadAction: () -> Unit,
                                                                       onNext: (T) -> Unit,
                                                                       onComplete: () -> Unit,
                                                                       onError: (Throwable) -> Unit): Disposable {
        return initializeAutoReload(this, autoReloadAction)
                .subscribeIoHandleErrorBy(onNext, onComplete, onError)
    }

    protected fun <T> Single<T>.subscribeIoHandleErrorAutoReloadBy(autoReloadAction: () -> Unit,
                                                                   onSuccess: (T) -> Unit,
                                                                   onError: (Throwable) -> Unit): Disposable {
        return initializeAutoReload(this, autoReloadAction)
                .subscribeIoHandleErrorBy(onSuccess, onError)
    }

    protected fun Completable.subscribeIoHandleErrorAutoReloadBy(autoReloadAction: () -> Unit,
                                                                 onComplete: () -> Unit,
                                                                 onError: (Throwable) -> Unit): Disposable {
        return initializeAutoReload(this, autoReloadAction)
                .subscribeIoHandleErrorBy(onComplete, onError)
    }

    protected fun <T> Maybe<T>.subscribeIoHandleErrorAutoReloadBy(autoReloadAction: () -> Unit,
                                                                  onSuccess: (T) -> Unit,
                                                                  onComplete: () -> Unit,
                                                                  onError: (Throwable) -> Unit): Disposable {
        return initializeAutoReload(this, autoReloadAction)
                .subscribeIoHandleErrorBy(onSuccess, onComplete, onError)
    }

    //endregion

    //region subscribeWithoutFreezing

    protected fun <T> Observable<T>.subscribeWithoutFreezingBy(onNext: (T) -> Unit,
                                                               onError: (Throwable) -> Unit): Disposable {
        return subscribeWithoutFreezingBy(LambdaObserver(onNext.asConsumerSafe(), onError.asErrorConsumerSafe(),
                Functions.EMPTY_ACTION, Functions.emptyConsumer()))
    }

    protected fun <T> Single<T>.subscribeWithoutFreezingBy(onSuccess: (T) -> Unit,
                                                           onError: (Throwable) -> Unit): Disposable {
        return subscribeWithoutFreezingBy(object : DisposableSingleObserver<T>() {
            override fun onSuccess(t: T) {
                onSuccess.invoke(t)
            }

            override fun onError(e: Throwable) {
                onError.invoke(e)
            }
        })
    }

    protected fun Completable.subscribeWithoutFreezingBy(onComplete: () -> Unit,
                                                         onError: (Throwable) -> Unit): Disposable {
        return subscribeWithoutFreezingBy(object : DisposableCompletableObserver() {
            override fun onComplete() {
                onComplete.invoke()
            }

            override fun onError(e: Throwable) {
                onError.invoke(e)
            }
        })
    }

    protected fun <T> Maybe<T>.subscribeWithoutFreezingBy(onSuccess: (T) -> Unit,
                                                          onComplete: () -> Unit,
                                                          onError: (Throwable) -> Unit): Disposable {
        return subscribeWithoutFreezingBy(object : DisposableMaybeObserver<T>() {

            override fun onSuccess(t: T) {
                onSuccess.invoke(t)
            }

            override fun onComplete() {
                onComplete.invoke()
            }

            override fun onError(e: Throwable) {
                onError.invoke(e)
            }
        })
    }

    //endregion

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
        return ConsumerSafe {
            cancelAutoReload(reloadAction)
            if (connectionProvider.isDisconnected) {
                val disposable = connectionProvider.observeConnectionChanges()
                        .filter { connected -> connected }
                        .firstElement()
                        .toObservable().subscribeBy({
                            reloadAction.invoke()
                            autoReloadDisposables.remove(reloadAction.hashCode())
                        }, {})
                autoReloadDisposables[reloadAction.hashCode()] = disposable
            }
        }
    }

    private fun cancelAutoReload(reloadAction: () -> Unit) {
        val disposable = autoReloadDisposables[reloadAction.hashCode()]
        if (isDisposableActive(disposable)) {
            disposable?.dispose()
        }
    }

    //region java compatible methods

    //region subscribe

    /**
     * @param replaceFrozenEventPredicate - used for reduce num element in freeze buffer
     * @see @link .subscribe
     * @see @link OperatorFreeze
     */
    protected fun <T> subscribe(observable: Observable<T>,
                                replaceFrozenEventPredicate: BiFunctionSafe<T, T, Boolean>,
                                onNext: ConsumerSafe<T>,
                                onError: ConsumerSafe<Throwable>): Disposable {
        return observable.subscribeBy(replaceFrozenEventPredicate, onNext.fromConsumer(), onError.fromErrorConsumer())
    }

    /**
     * @see @link .subscribe
     */
    protected fun <T> subscribe(observable: Observable<T>,
                                onNext: ConsumerSafe<T>): Disposable {
        return observable.subscribeBy(onNext.fromConsumer())
    }

    /**
     * @see @link .subscribe
     */
    protected fun <T> subscribe(observable: Observable<T>,
                                onNext: ConsumerSafe<T>,
                                onError: ConsumerSafe<Throwable>): Disposable {
        return observable.subscribeBy(onNext.fromConsumer(), onError.fromErrorConsumer())
    }

    /**
     * @see @link .subscribe
     */
    protected fun <T> subscribe(observable: Observable<T>,
                                onNext: ConsumerSafe<T>,
                                onComplete: ActionSafe,
                                onError: ConsumerSafe<Throwable>): Disposable {
        return observable.subscribeBy(onNext.fromConsumer(), onComplete.fromCompleteAction(), onError.fromErrorConsumer())
    }

    protected fun <T> subscribe(single: Single<T>,
                                onSuccess: ConsumerSafe<T>,
                                onError: ConsumerSafe<Throwable>): Disposable {
        return single.subscribeBy(onSuccess.fromConsumer(), onError.fromErrorConsumer())
    }

    protected fun subscribe(completable: Completable,
                            onComplete: ActionSafe,
                            onError: ConsumerSafe<Throwable>): Disposable {
        return completable.subscribeBy(onComplete.fromCompleteAction(), onError.fromErrorConsumer())
    }

    protected fun <T> subscribe(maybe: Maybe<T>,
                                onSuccess: ConsumerSafe<T>,
                                onComplete: ActionSafe,
                                onError: ConsumerSafe<Throwable>): Disposable {
        return maybe.subscribeBy(onSuccess.fromConsumer(), onComplete.fromCompleteAction(), onError.fromErrorConsumer())
    }

    /**
     * @see @link .subscribeWithoutFreezing
     */
    protected fun <T> subscribeWithoutFreezing(observable: Observable<T>,
                                               onNext: ConsumerSafe<T>,
                                               onError: ConsumerSafe<Throwable>): Disposable {
        return observable.subscribeWithoutFreezingBy(onNext.fromConsumer(), onError.fromErrorConsumer())
    }

    protected fun <T> subscribeWithoutFreezing(single: Single<T>,
                                               onSuccess: ConsumerSafe<T>,
                                               onError: ConsumerSafe<Throwable>): Disposable {
        return single.subscribeWithoutFreezingBy(onSuccess.fromConsumer(), onError.fromErrorConsumer())
    }

    protected fun subscribeWithoutFreezing(completable: Completable,
                                           onComplete: ActionSafe,
                                           onError: ConsumerSafe<Throwable>): Disposable {
        return completable.subscribeWithoutFreezingBy(onComplete.fromCompleteAction(), onError.fromErrorConsumer())
    }

    protected fun <T> subscribeWithoutFreezing(maybe: Maybe<T>,
                                               onSuccess: ConsumerSafe<T>,
                                               onComplete: ActionSafe,
                                               onError: ConsumerSafe<Throwable>): Disposable {
        return maybe.subscribeWithoutFreezingBy(onSuccess.fromConsumer(), onComplete.fromCompleteAction(), onError.fromErrorConsumer())
    }

    //endregion

    //region subscribeIoHandleError

    /**
     * Работает также как [.subscribe], кроме того автоматически обрабатывает ошибки,
     * см [ErrorHandler] и переводит выполенения потока в фон
     */
    protected fun <T> subscribeIoHandleError(observable: Observable<T>,
                                             onNext: ConsumerSafe<T>): Disposable {
        return observable.subscribeIoHandleErrorBy(onNext.fromConsumer())
    }

    protected fun <T> subscribeIoHandleError(single: Single<T>,
                                             onSuccess: ConsumerSafe<T>): Disposable {
        return single.subscribeIoHandleErrorBy(onSuccess.fromConsumer())
    }

    protected fun <T> subscribeIoHandleError(maybe: Maybe<T>,
                                             onSuccess: ConsumerSafe<T>,
                                             onComplete: ActionSafe): Disposable {
        return maybe.subscribeIoHandleErrorBy(onSuccess.fromConsumer(), onComplete.fromCompleteAction())
    }

    protected fun <T> subscribeIoHandleError(observable: Observable<T>,
                                             onNext: ConsumerSafe<T>,
                                             onError: ConsumerSafe<Throwable>): Disposable {
        return observable.subscribeIoHandleErrorBy(onNext.fromConsumer(), onError.fromErrorConsumer())
    }

    protected fun <T> subscribeIoHandleError(observable: Observable<T>,
                                             onNext: ConsumerSafe<T>,
                                             onComplete: ActionSafe,
                                             onError: ConsumerSafe<Throwable>): Disposable {
        return observable.subscribeIoHandleErrorBy(onNext.fromConsumer(), onComplete.fromCompleteAction(), onError.fromErrorConsumer())
    }

    protected fun <T> subscribeIoHandleError(single: Single<T>,
                                             onSuccess: ConsumerSafe<T>,
                                             onError: ConsumerSafe<Throwable>): Disposable {
        return single.subscribeIoHandleErrorBy(onSuccess.fromConsumer(), onError.fromErrorConsumer())
    }

    protected fun subscribeIoHandleError(completable: Completable,
                                         onComplete: ActionSafe,
                                         onError: ConsumerSafe<Throwable>): Disposable {
        return completable.subscribeIoHandleErrorBy(onComplete.fromCompleteAction(), onError.fromErrorConsumer())
    }

    protected fun <T> subscribeIoHandleError(maybe: Maybe<T>,
                                             onSuccess: ConsumerSafe<T>,
                                             onComplete: ActionSafe,
                                             onError: ConsumerSafe<Throwable>): Disposable {
        return maybe.subscribeIoHandleErrorBy(onSuccess.fromConsumer(), onComplete.fromCompleteAction(), onError.fromErrorConsumer())
    }

    //endregion

    //region subscribeIo
    protected fun <T> subscribeIo(observable: Observable<T>,
                                  onNext: ConsumerSafe<T>,
                                  onError: ConsumerSafe<Throwable>): Disposable {
        return observable.subscribeIoBy(onNext.fromConsumer(), onError.fromErrorConsumer())
    }

    protected fun <T> subscribeIo(single: Single<T>,
                                  onSuccess: ConsumerSafe<T>,
                                  onError: ConsumerSafe<Throwable>): Disposable {
        return single.subscribeIoBy(onSuccess.fromConsumer(), onError.fromErrorConsumer())
    }

    protected fun subscribeIo(completable: Completable,
                              onComplete: ActionSafe,
                              onError: ConsumerSafe<Throwable>): Disposable {
        return completable.subscribeIoBy(onComplete.fromCompleteAction(), onError.fromErrorConsumer())
    }

    protected fun <T> subscribeIo(observable: Observable<T>,
                                  onNext: ConsumerSafe<T>,
                                  onComplete: ActionSafe,
                                  onError: ConsumerSafe<Throwable>): Disposable {
        return observable.subscribeIoBy(onNext.fromConsumer(), onComplete.fromCompleteAction(), onError.fromErrorConsumer())
    }

    protected fun <T> subscribeIo(maybe: Maybe<T>,
                                  onSuccess: ConsumerSafe<T>,
                                  onComplete: ActionSafe,
                                  onError: ConsumerSafe<Throwable>): Disposable {
        return maybe.subscribeIoBy(onSuccess.fromConsumer(), onComplete.fromCompleteAction(), onError.fromErrorConsumer())
    }
    //endregion

    //region subscribeIoAutoReload

    /**
     * {@see subscribeIo}
     * автоматически вызовет autoReloadAction при появлении интернета если на момент выполнения
     * observable не было подключения к интернету
     */
    protected fun <T> subscribeIoAutoReload(observable: Observable<T>,
                                            autoReloadAction: ActionSafe,
                                            onNext: ConsumerSafe<T>,
                                            onError: ConsumerSafe<Throwable>): Disposable {
        return subscribe(initializeAutoReload(observable, autoReloadAction.fromCompleteAction()), onNext, onError)
    }

    protected fun <T> subscribeIoAutoReload(observable: Observable<T>,
                                            autoReloadAction: ActionSafe,
                                            onNext: ConsumerSafe<T>,
                                            onComplete: ActionSafe,
                                            onError: ConsumerSafe<Throwable>): Disposable {
        return subscribe(initializeAutoReload(observable, autoReloadAction.fromCompleteAction()), onNext, onComplete, onError)
    }

    protected fun <T> subscribeIoAutoReload(single: Single<T>,
                                            autoReloadAction: ActionSafe,
                                            onSuccess: ConsumerSafe<T>,
                                            onError: ConsumerSafe<Throwable>): Disposable {
        return subscribe(initializeAutoReload(single, autoReloadAction.fromCompleteAction()), onSuccess, onError)
    }

    protected fun subscribeIoAutoReload(completable: Completable,
                                        autoReloadAction: ActionSafe,
                                        onComplete: ActionSafe,
                                        onError: ConsumerSafe<Throwable>): Disposable {
        return subscribe(initializeAutoReload(completable, autoReloadAction.fromCompleteAction()), onComplete, onError)
    }

    protected fun <T> subscribeIoAutoReload(maybe: Maybe<T>,
                                            autoReloadAction: ActionSafe,
                                            onSuccess: ConsumerSafe<T>,
                                            onComplete: ActionSafe,
                                            onError: ConsumerSafe<Throwable>): Disposable {
        return subscribe(initializeAutoReload(maybe, autoReloadAction.fromCompleteAction()), onSuccess, onComplete, onError)
    }
    //endregion

    //region subscribeIoHandleErrorAutoReload

    /**
     * {@see subscribeIoAutoReload} кроме того автоматически обрабатывает ошибки
     */
    protected fun <T> subscribeIoHandleErrorAutoReload(observable: Observable<T>,
                                                       autoReloadAction: ActionSafe,
                                                       onNext: ConsumerSafe<T>,
                                                       onError: ConsumerSafe<Throwable>): Disposable {
        return subscribeIoHandleError(initializeAutoReload(observable, autoReloadAction.fromCompleteAction()), onNext, onError)
    }

    protected fun <T> subscribeIoHandleErrorAutoReload(observable: Observable<T>,
                                                       autoReloadAction: ActionSafe,
                                                       onNext: ConsumerSafe<T>,
                                                       onComplete: ActionSafe,
                                                       onError: ConsumerSafe<Throwable>): Disposable {
        return subscribeIoHandleError(initializeAutoReload(observable, autoReloadAction.fromCompleteAction()), onNext, onComplete, onError)
    }

    protected fun <T> subscribeIoHandleErrorAutoReload(single: Single<T>,
                                                       autoReloadAction: ActionSafe,
                                                       onSuccess: ConsumerSafe<T>,
                                                       onError: ConsumerSafe<Throwable>): Disposable {
        return subscribeIoHandleError(initializeAutoReload(single, autoReloadAction.fromCompleteAction()), onSuccess, onError)
    }

    protected fun subscribeIoHandleErrorAutoReload(completable: Completable,
                                                   autoReloadAction: ActionSafe,
                                                   onComplete: ActionSafe,
                                                   onError: ConsumerSafe<Throwable>): Disposable {
        return subscribeIoHandleError(initializeAutoReload(completable, autoReloadAction.fromCompleteAction()), onComplete, onError)
    }

    protected fun <T> subscribeIoHandleErrorAutoReload(maybe: Maybe<T>,
                                                       autoReloadAction: ActionSafe,
                                                       onSuccess: ConsumerSafe<T>,
                                                       onComplete: ActionSafe,
                                                       onError: ConsumerSafe<Throwable>): Disposable {
        return subscribeIoHandleError(initializeAutoReload(maybe, autoReloadAction.fromCompleteAction()), onSuccess, onComplete, onError)
    }

    //endregion

    //endregion

    // TODO remove
    // Deprecated methods wil be removed

    //region subscribeIoHandleError

    /**
     * Работает также как [.subscribe], кроме того автоматически обрабатывает ошибки,
     * см [ErrorHandler] и переводит выполенения потока в фон
     */
    @Deprecated("Use extension instead", ReplaceWith("observable.subscribeIoHandleErrorBy(onNext)"))
    protected fun <T> subscribeIoHandleError(observable: Observable<T>,
                                             onNext: (T) -> Unit): Disposable {
        return observable.subscribeIoHandleErrorBy(onNext, onErrorStub)
    }

    @Deprecated("Use extension instead", ReplaceWith("single.subscribeIoHandleErrorBy(onSuccess)"))
    protected fun <T> subscribeIoHandleError(single: Single<T>,
                                             onSuccess: (T) -> Unit): Disposable {
        return single.subscribeIoHandleErrorBy(onSuccess, onErrorStub)
    }

    @Deprecated("Use extension instead", ReplaceWith("single.subscribeIoHandleErrorBy(onComplete)"))
    protected fun subscribeIoHandleError(completable: Completable,
                                         onComplete: () -> Unit): Disposable {
        return completable.subscribeIoHandleErrorBy(onComplete, onErrorStub)
    }

    @Deprecated("Use extension instead", ReplaceWith("maybe.subscribeIoHandleErrorBy(onSuccess, onComplete)"))
    protected fun <T> subscribeIoHandleError(maybe: Maybe<T>,
                                             onSuccess: (T) -> Unit,
                                             onComplete: () -> Unit): Disposable {
        return maybe.subscribeIoHandleErrorBy(onSuccess, onComplete, onErrorStub)
    }

    @Deprecated("Use extension instead", ReplaceWith("observable.subscribeIoHandleErrorBy(onNext, onError)"))
    protected fun <T> subscribeIoHandleError(observable: Observable<T>,
                                             onNext: (T) -> Unit,
                                             onError: (Throwable) -> Unit): Disposable {
        return observable.subscribeIoHandleErrorBy(onNext, { e -> handleError(e, onError) })
    }

    @Deprecated("Use extension instead", ReplaceWith("observable.subscribeIoHandleErrorBy(onNext, onComplete, onError)"))
    protected fun <T> subscribeIoHandleError(observable: Observable<T>,
                                             onNext: (T) -> Unit,
                                             onComplete: () -> Unit,
                                             onError: (Throwable) -> Unit): Disposable {
        return observable.subscribeIoHandleErrorBy(onNext, onComplete, { e -> handleError(e, onError) })
    }

    @Deprecated("Use extension instead", ReplaceWith("single.subscribeIoHandleErrorBy(onSuccess, onError)"))
    protected fun <T> subscribeIoHandleError(single: Single<T>,
                                             onSuccess: (T) -> Unit,
                                             onError: (Throwable) -> Unit): Disposable {
        return single.subscribeIoHandleErrorBy(onSuccess, { e -> handleError(e, onError) })
    }

    @Deprecated("Use extension instead", ReplaceWith("completable.subscribeIoHandleErrorBy(onComplete, onError)"))
    protected fun subscribeIoHandleError(completable: Completable,
                                         onComplete: () -> Unit,
                                         onError: (Throwable) -> Unit): Disposable {
        return completable.subscribeIoHandleErrorBy(onComplete, { e -> handleError(e, onError) })
    }

    @Deprecated("Use extension instead", ReplaceWith("maybe.subscribeIoHandleErrorBy(onSuccess, onComplete, onError)"))
    protected fun <T> subscribeIoHandleError(maybe: Maybe<T>,
                                             onSuccess: (T) -> Unit,
                                             onComplete: () -> Unit,
                                             onError: (Throwable) -> Unit): Disposable {
        return maybe.subscribeIoHandleErrorBy(onSuccess, onComplete, { e -> handleError(e, onError) })
    }

    //endregion

    //region subscribeIo
    @Deprecated("Use extension instead", ReplaceWith("observable.subscribeIoBy(onNext, onError)"))
    protected fun <T> subscribeIo(observable: Observable<T>,
                                  onNext: (T) -> Unit,
                                  onError: (Throwable) -> Unit): Disposable {
        return observable.subscribeIoBy(onNext, onError)
    }

    @Deprecated("Use extension instead", ReplaceWith("single.subscribeIoBy(onSuccess, onError)"))
    protected fun <T> subscribeIo(single: Single<T>,
                                  onSuccess: (T) -> Unit,
                                  onError: (Throwable) -> Unit): Disposable {
        return single.subscribeIoBy(onSuccess, onError)
    }

    @Deprecated("Use extension instead", ReplaceWith("completable.subscribeIoBy(onComplete, onError)"))
    protected fun subscribeIo(completable: Completable,
                              onComplete: () -> Unit,
                              onError: (Throwable) -> Unit): Disposable {
        return completable.subscribeIoBy(onComplete, onError)
    }

    @Deprecated("Use extension instead", ReplaceWith("observable.subscribeIoBy(onNext, onComplete, onError)"))
    protected fun <T> subscribeIo(observable: Observable<T>,
                                  onNext: (T) -> Unit,
                                  onComplete: () -> Unit,
                                  onError: (Throwable) -> Unit): Disposable {
        return observable.subscribeIoBy(onNext, onComplete, onError)
    }

    @Deprecated("Use extension instead", ReplaceWith("maybe.subscribeIoBy(onSuccess, onComplete, onError)"))
    protected fun <T> subscribeIo(maybe: Maybe<T>,
                                  onSuccess: (T) -> Unit,
                                  onComplete: () -> Unit,
                                  onError: (Throwable) -> Unit): Disposable {
        return maybe.subscribeIoBy(onSuccess, onComplete, onError)
    }
    //endregion

    //region subscribeIoAutoReload

    /**
     * {@see subscribeIo}
     * автоматически вызовет autoReloadAction при появлении интернета если на момент выполнения
     * observable не было подключения к интернету
     */
    @Deprecated("Use extension instead", ReplaceWith("observable.subscribeIoAutoReloadBy(autoReloadAction, onNext, onError)"))
    protected fun <T> subscribeIoAutoReload(observable: Observable<T>,
                                            autoReloadAction: () -> Unit,
                                            onNext: (T) -> Unit,
                                            onError: (Throwable) -> Unit): Disposable {
        return observable.subscribeIoAutoReloadBy(autoReloadAction, onNext, onError)
    }

    @Deprecated("Use extension instead", ReplaceWith("observable.subscribeIoAutoReloadBy(autoReloadAction, onNext, onComplete, onError)"))
    protected fun <T> subscribeIoAutoReload(observable: Observable<T>,
                                            autoReloadAction: () -> Unit,
                                            onNext: (T) -> Unit,
                                            onComplete: () -> Unit,
                                            onError: (Throwable) -> Unit): Disposable {
        return observable.subscribeIoAutoReloadBy(autoReloadAction, onNext, onComplete, onError)
    }

    @Deprecated("Use extension instead", ReplaceWith("single.subscribeIoAutoReloadBy(autoReloadAction, onSuccess, onError)"))
    protected fun <T> subscribeIoAutoReload(single: Single<T>,
                                            autoReloadAction: () -> Unit,
                                            onSuccess: (T) -> Unit,
                                            onError: (Throwable) -> Unit): Disposable {
        return single.subscribeIoAutoReloadBy(autoReloadAction, onSuccess, onError)
    }

    @Deprecated("Use extension instead", ReplaceWith("completable.subscribeIoAutoReloadBy(autoReloadAction, onComplete, onError)"))
    protected fun subscribeIoAutoReload(completable: Completable,
                                        autoReloadAction: () -> Unit,
                                        onComplete: () -> Unit,
                                        onError: (Throwable) -> Unit): Disposable {
        return completable.subscribeIoAutoReloadBy(autoReloadAction, onComplete, onError)
    }

    @Deprecated("Use extension instead", ReplaceWith("maybe.subscribeIoAutoReloadBy(autoReloadAction, onSuccess, onComplete, onError)"))
    protected fun <T> subscribeIoAutoReload(maybe: Maybe<T>,
                                            autoReloadAction: () -> Unit,
                                            onSuccess: (T) -> Unit,
                                            onComplete: () -> Unit,
                                            onError: (Throwable) -> Unit): Disposable {
        return maybe.subscribeIoAutoReloadBy(autoReloadAction, onSuccess, onComplete, onError)
    }
    //endregion

    //region subscribeIoHandleErrorAutoReload

    /**
     * {@see subscribeIoAutoReload} кроме того автоматически обрабатывает ошибки
     */
    @Deprecated("Use extension instead", ReplaceWith("observable.subscribeIoHandleErrorAutoReloadBy(autoReloadAction, onNext, onError)"))
    protected fun <T> subscribeIoHandleErrorAutoReload(observable: Observable<T>,
                                                       autoReloadAction: () -> Unit,
                                                       onNext: (T) -> Unit,
                                                       onError: (Throwable) -> Unit): Disposable {
        return initializeAutoReload(observable, autoReloadAction).subscribeIoHandleErrorBy(onNext, onError)
    }

    @Deprecated("Use extension instead", ReplaceWith("observable.subscribeIoHandleErrorAutoReloadBy(autoReloadAction, onNext, onComplete, onError)"))
    protected fun <T> subscribeIoHandleErrorAutoReload(observable: Observable<T>,
                                                       autoReloadAction: () -> Unit,
                                                       onNext: (T) -> Unit,
                                                       onComplete: () -> Unit,
                                                       onError: (Throwable) -> Unit): Disposable {
        return initializeAutoReload(observable, autoReloadAction).subscribeIoHandleErrorBy(onNext, onComplete, onError)
    }

    @Deprecated("Use extension instead", ReplaceWith("single.subscribeIoHandleErrorAutoReloadBy(autoReloadAction, onSuccess, onError)"))
    protected fun <T> subscribeIoHandleErrorAutoReload(single: Single<T>,
                                                       autoReloadAction: () -> Unit,
                                                       onSuccess: (T) -> Unit,
                                                       onError: (Throwable) -> Unit): Disposable {
        return initializeAutoReload(single, autoReloadAction).subscribeIoHandleErrorBy(onSuccess, onError)
    }

    @Deprecated("Use extension instead", ReplaceWith("completable.subscribeIoHandleErrorAutoReloadBy(autoReloadAction, onComplete, onError)"))
    protected fun subscribeIoHandleErrorAutoReload(completable: Completable,
                                                   autoReloadAction: () -> Unit,
                                                   onComplete: () -> Unit,
                                                   onError: (Throwable) -> Unit): Disposable {
        return initializeAutoReload(completable, autoReloadAction).subscribeIoHandleErrorBy(onComplete, onError)
    }

    @Deprecated("Use extension instead", ReplaceWith("maybe.subscribeIoHandleErrorAutoReloadBy(autoReloadAction, onSuccess, onComplete, onError)"))
    protected fun <T> subscribeIoHandleErrorAutoReload(maybe: Maybe<T>,
                                                       autoReloadAction: () -> Unit,
                                                       onSuccess: (T) -> Unit,
                                                       onComplete: () -> Unit,
                                                       onError: (Throwable) -> Unit): Disposable {
        return initializeAutoReload(maybe, autoReloadAction).subscribeIoHandleErrorBy(onSuccess, onComplete, onError)
    }

    //endregion
}
