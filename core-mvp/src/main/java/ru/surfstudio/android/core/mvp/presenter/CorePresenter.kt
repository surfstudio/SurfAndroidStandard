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
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.exceptions.OnErrorNotImplementedException
import io.reactivex.internal.functions.Functions
import io.reactivex.internal.observers.LambdaObserver
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.observers.DisposableMaybeObserver
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.subjects.BehaviorSubject
import ru.surfstudio.android.core.mvp.view.CoreView
import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager
import ru.surfstudio.android.core.ui.state.ScreenState
import ru.surfstudio.android.rx.extension.ActionSafe
import ru.surfstudio.android.rx.extension.BiFunctionSafe
import ru.surfstudio.android.rx.extension.ConsumerSafe

/**
 * базовый класс презентера, содержащий всю корневую логику
 * Методы [.subscribe] добавляют логику замораживания
 * Rx событий на время пересоздания вью или когда экран находится в фоне, см [ObservableOperatorFreeze]
 * Также все подписки освобождаются при полном уничтожении экрана
 *
 * @param <V>
</V> */
abstract class CorePresenter<V : CoreView?>(eventDelegateManager: ScreenEventDelegateManager, screenState: ScreenState) : Presenter<V> {

    private val onCompleteStub: () -> Unit = {}
    private val onErrorNotImplemented: (Throwable) -> Unit = { throwable -> RxJavaPlugins.onError(OnErrorNotImplementedException(throwable)) }

    private val disposables = CompositeDisposable()
    private val freezeSelector = BehaviorSubject.createDefault(false)

    /**
     * @return screen's view
     */
    private var viewInternal: V? = null

    val view: V
        get() = viewInternal!!

    private var freezeEventsOnPause = true

    /**
     * см [StateRestorer]
     *
     * @return
     */
    val stateRestorer: StateRestorer<*>?
        get() = null

    init {
        eventDelegateManager.registerDelegate(CorePresenterGateway(this, screenState))
    }

    override fun attachView(view: V) {
        this.viewInternal = view
    }

    override fun onLoad(viewRecreated: Boolean) {}

    override fun onFirstLoad() {}

    override fun onStart() {

    }

    override fun onResume() {
        freezeSelector.onNext(false)
    }

    override fun onPause() {
        if (freezeEventsOnPause) {
            freezeSelector.onNext(true)
        }
    }

    override fun onStop() {

    }

    override fun detachView() {
        onViewDetach()
        viewInternal = null
    }

    override fun onViewDetach() {
        freezeSelector.onNext(true)
    }

    override fun onDestroy() {
        disposables.dispose()
    }

    /**
     * If true, all rx event would be frozen when screen paused, and unfrozen when screen resumed,
     * otherwise event would be frozen when [.onViewDetach] called.
     * Default enabled.
     *
     * @param enabled
     */
    fun setFreezeOnPauseEnabled(enabled: Boolean) {
        this.freezeEventsOnPause = enabled
    }

    protected fun isDisposableInactive(disposable: Disposable?): Boolean {
        return disposable == null || disposable.isDisposed
    }

    protected fun isDisposableActive(disposable: Disposable?): Boolean {
        return disposable != null && !disposable.isDisposed
    }

    /**
     * Apply [ObservableOperatorFreeze] and subscribe subscriber to the observable.
     * When screen finally destroyed, all subscriptions would be automatically unsubscribed.
     * For more information see description of this class.
     *
     * @return subscription
     */
    protected fun <T> Observable<T>.subscribeBy(operator: ObservableOperatorFreeze<T>,
                                                observer: LambdaObserver<T>): Disposable {
        val disposable = this
                .lift(operator)
                .subscribeWith(observer)
        disposables.add(disposable)
        return disposable
    }

    protected fun <T> Single<T>.subscribeBy(operator: SingleOperatorFreeze<T>,
                                            observer: DisposableSingleObserver<T>): Disposable {

        val disposable = this
                .lift(operator)
                .subscribeWith(observer)
        disposables.add(disposable)
        return disposable
    }

    protected fun <T> Maybe<T>.subscribeBy(operator: MaybeOperatorFreeze<T>,
                                           observer: DisposableMaybeObserver<T>): Disposable {
        val disposable = this
                .lift(operator)
                .subscribeWith(observer)
        disposables.add(disposable)
        return disposable
    }

    protected fun Completable.subscribeBy(operator: CompletableOperatorFreeze,
                                          observer: DisposableCompletableObserver): Disposable {
        val disposable = this
                .lift(operator)
                .subscribeWith(observer)
        disposables.add(disposable)
        return disposable
    }

    protected fun <T> Observable<T>.subscribeBy(observer: LambdaObserver<T>): Disposable {
        return this.subscribeBy(ObservableOperatorFreeze(freezeSelector), observer)
    }

    protected fun <T> Observable<T>.subscribeBy(onNext: (T) -> Unit): Disposable {

        return this.subscribeBy(ObservableOperatorFreeze(freezeSelector), onNext, onErrorNotImplemented)
    }

    protected fun <T> Observable<T>.subscribeBy(onNext: (T) -> Unit,
                                                onError: (Throwable) -> Unit): Disposable {

        return this.subscribeBy(onNext, onCompleteStub, onError)
    }


    protected fun <T> Observable<T>.subscribeBy(onNext: (T) -> Unit,
                                                onComplete: () -> Unit,
                                                onError: (Throwable) -> Unit): Disposable {

        return this.subscribeBy(ObservableOperatorFreeze(freezeSelector), onNext, onComplete, onError)
    }

    protected fun <T> Single<T>.subscribeBy(onSuccess: (T) -> Unit,
                                            onError: (Throwable) -> Unit): Disposable {

        return this.subscribeBy(SingleOperatorFreeze(freezeSelector), onSuccess, onError)
    }

    protected fun Completable.subscribeBy(onComplete: () -> Unit,
                                          onError: (Throwable) -> Unit): Disposable {

        return this.subscribeBy(CompletableOperatorFreeze(freezeSelector), onComplete, onError)
    }

    protected fun <T> Maybe<T>.subscribeBy(onSuccess: (T) -> Unit,
                                           onComplete: () -> Unit,
                                           onError: (Throwable) -> Unit): Disposable {

        return this.subscribeBy(MaybeOperatorFreeze(freezeSelector), onSuccess, onComplete, onError)
    }

    /**
     * @param replaceFrozenEventPredicate - used for reduce num element in freeze buffer
     * @see ObservableOperatorFreeze
     */
    protected fun <T> Observable<T>.subscribeBy(replaceFrozenEventPredicate: BiFunctionSafe<T, T, Boolean>,
                                                observer: LambdaObserver<T>): Disposable {

        return this.subscribeBy(ObservableOperatorFreeze(freezeSelector, replaceFrozenEventPredicate), observer)
    }

    /**
     * @param replaceFrozenEventPredicate - used for reduce num element in freeze buffer
     * @see @link .subscribe
     * @see @link OperatorFreeze
     */
    protected fun <T> Observable<T>.subscribeBy(replaceFrozenEventPredicate: BiFunctionSafe<T, T, Boolean>,
                                                onNext: (T) -> Unit,
                                                onError: (Throwable) -> Unit): Disposable {

        return this.subscribeBy(ObservableOperatorFreeze(freezeSelector, replaceFrozenEventPredicate), onNext, onError)
    }

    protected fun <T> Observable<T>.subscribeBy(operator: ObservableOperatorFreeze<T>,
                                                onNext: (T) -> Unit,
                                                onError: (Throwable) -> Unit): Disposable {
        return this.subscribeBy(operator, onNext, onCompleteStub, onError)
    }

    protected fun <T> Observable<T>.subscribeBy(operator: ObservableOperatorFreeze<T>,
                                                onNext: (T) -> Unit,
                                                onComplete: () -> Unit,
                                                onError: (Throwable) -> Unit): Disposable {
        return this.subscribeBy(operator, LambdaObserver(onNext.asConsumerSafe(), onError.asErrorConsumerSafe(), onComplete.asActionSafe(), Functions.emptyConsumer()))
    }

    protected fun <T> Single<T>.subscribeBy(operator: SingleOperatorFreeze<T>,
                                            onSuccess: (T) -> Unit,
                                            onError: (Throwable) -> Unit): Disposable {
        return subscribeBy(operator, object : DisposableSingleObserver<T>() {
            override fun onSuccess(t: T) {
                onSuccess.invoke(t)
            }

            override fun onError(e: Throwable) {
                onError.invoke(e)
            }
        })
    }

    protected fun <T> Maybe<T>.subscribeBy(operator: MaybeOperatorFreeze<T>,
                                           onSuccess: (T) -> Unit,
                                           onComplete: () -> Unit,
                                           onError: (Throwable) -> Unit): Disposable {
        return subscribeBy(operator, object : DisposableMaybeObserver<T>() {
            override fun onSuccess(t: T) {
                onSuccess.invoke(t)
            }

            override fun onError(e: Throwable) {
                onError.invoke(e)
            }

            override fun onComplete() {
                onComplete.invoke()
            }
        })
    }

    protected fun Completable.subscribeBy(operator: CompletableOperatorFreeze,
                                          onComplete: () -> Unit,
                                          onError: (Throwable) -> Unit): Disposable {
        return subscribeBy(operator, object : DisposableCompletableObserver() {
            override fun onComplete() {
                onComplete.invoke()
            }

            override fun onError(e: Throwable) {
                onError.invoke(e)
            }
        })
    }

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

    /**
     * Subscribe subscriber to the observable without applying [ObservableOperatorFreeze]
     * When screen finally destroyed, all subscriptions would be automatically unsubscribed.
     *
     * @return subscription
     */
    protected fun <T> Observable<T>.subscribeWithoutFreezingBy(subscriber: LambdaObserver<T>): Disposable {
        val disposable = this.subscribeWith(subscriber)
        disposables.add(disposable)
        return disposable
    }

    protected fun <T> Single<T>.subscribeWithoutFreezingBy(subscriber: DisposableSingleObserver<T>): Disposable {
        val disposable = this.subscribeWith(subscriber)
        disposables.add(disposable)
        return disposable
    }

    protected fun Completable.subscribeWithoutFreezingBy(subscriber: DisposableCompletableObserver): Disposable {
        val disposable = this.subscribeWith(subscriber)
        disposables.add(disposable)
        return disposable
    }

    protected fun <T> Maybe<T>.subscribeWithoutFreezingBy(subscriber: DisposableMaybeObserver<T>): Disposable {
        val disposable = this.subscribeWith(subscriber)
        disposables.add(disposable)
        return disposable
    }

    //region java compatible methods

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

    //TODO remove
    //deprecated methods will be removed
    @Deprecated("Use extension instead",
            ReplaceWith("observable.subscribeBy(operator, onNext, onError)"))
    protected fun <T> subscribe(observable: Observable<T>,
                                operator: ObservableOperatorFreeze<T>,
                                onNext: (T) -> Unit,
                                onError: (Throwable) -> Unit): Disposable {
        return observable.subscribeBy(operator, onNext, onCompleteStub, onError)
    }

    @Deprecated("Use extension instead", ReplaceWith("observable.subscribeBy(operator, onNext, onComplete, onError)"))
    protected fun <T> subscribe(observable: Observable<T>,
                                operator: ObservableOperatorFreeze<T>,
                                onNext: (T) -> Unit,
                                onComplete: () -> Unit,
                                onError: (Throwable) -> Unit): Disposable {
        return observable.subscribeBy(operator, LambdaObserver(onNext.asConsumerSafe(), onError.asErrorConsumerSafe(), onComplete.asActionSafe(), Functions.emptyConsumer()))
    }

    @Deprecated("Use extension instead", ReplaceWith("single.subscribeBy(operator, onSuccess, onError)"))
    protected fun <T> subscribe(single: Single<T>,
                                operator: SingleOperatorFreeze<T>,
                                onSuccess: (T) -> Unit,
                                onError: (Throwable) -> Unit): Disposable {
        return single.subscribeBy(operator, object : DisposableSingleObserver<T>() {
            override fun onSuccess(t: T) {
                onSuccess.invoke(t)
            }

            override fun onError(e: Throwable) {
                onError.invoke(e)
            }
        })
    }

    @Deprecated("Use extension instead", ReplaceWith("maybe.subscribeBy(operator, onSuccess, onComplete, onError)"))
    protected fun <T> subscribe(maybe: Maybe<T>,
                                operator: MaybeOperatorFreeze<T>,
                                onSuccess: (T) -> Unit,
                                onComplete: () -> Unit,
                                onError: (Throwable) -> Unit): Disposable {
        return maybe.subscribeBy(operator, object : DisposableMaybeObserver<T>() {
            override fun onSuccess(t: T) {
                onSuccess.invoke(t)
            }

            override fun onError(e: Throwable) {
                onError.invoke(e)
            }

            override fun onComplete() {
                onComplete.invoke()
            }
        })
    }

    @Deprecated("Use extension instead", ReplaceWith("completable.subscribeBy(operator, onComplete, onError)"))
    protected fun subscribe(completable: Completable,
                            operator: CompletableOperatorFreeze,
                            onComplete: () -> Unit,
                            onError: (Throwable) -> Unit): Disposable {
        return completable.subscribeBy(operator, object : DisposableCompletableObserver() {
            override fun onComplete() {
                onComplete.invoke()
            }

            override fun onError(e: Throwable) {
                onError.invoke(e)
            }
        })
    }

    /**
     * @param replaceFrozenEventPredicate - used for reduce num element in freeze buffer
     * @see .subscribe
     * @see ObservableOperatorFreeze
     */
    @Deprecated("Use extension instead", ReplaceWith("observable.subscribeBy(replaceFrozenEventPredicate, observer)"))
    protected fun <T> subscribe(observable: Observable<T>,
                                replaceFrozenEventPredicate: BiFunctionSafe<T, T, Boolean>,
                                observer: LambdaObserver<T>): Disposable {

        return observable.subscribeBy(ObservableOperatorFreeze<T>(freezeSelector, replaceFrozenEventPredicate), observer)
    }


    /**
     * @param replaceFrozenEventPredicate - used for reduce num element in freeze buffer
     * @see @link .subscribe
     * @see @link OperatorFreeze
     */
    @Deprecated("Use extension instead", ReplaceWith("observable.subscribeBy(replaceFrozenEventPredicate, onNext, onError)"))
    protected fun <T> subscribe(observable: Observable<T>,
                                replaceFrozenEventPredicate: BiFunctionSafe<T, T, Boolean>,
                                onNext: (T) -> Unit,
                                onError: (Throwable) -> Unit): Disposable {

        return observable.subscribeBy(ObservableOperatorFreeze<T>(freezeSelector, replaceFrozenEventPredicate), onNext, onError)
    }

    /**
     * @see @link .subscribe
     */
    @Deprecated("Use extension instead", ReplaceWith("observable.subscribeBy(replaceFrozenEventPredicate, observer)"))
    protected fun <T> subscribe(observable: Observable<T>,
                                observer: LambdaObserver<T>): Disposable {

        return observable.subscribeBy(ObservableOperatorFreeze(freezeSelector), observer)
    }


    /**
     * @see @link .subscribe
     */
    @Deprecated("Use extension instead", ReplaceWith("observable.subscribeBy(onNext)"))
    protected fun <T> subscribe(observable: Observable<T>,
                                onNext: (T) -> Unit): Disposable {

        return observable.subscribeBy(ObservableOperatorFreeze(freezeSelector), onNext, onErrorNotImplemented)
    }

    /**
     * @see @link .subscribe
     */
    @Deprecated("Use extension instead", ReplaceWith("observable.subscribeBy(onNext, onError)"))
    protected fun <T> subscribe(observable: Observable<T>,
                                onNext: (T) -> Unit,
                                onError: (Throwable) -> Unit): Disposable {

        return observable.subscribeBy(onNext, onCompleteStub, onError)
    }


    /**
     * @see @link .subscribe
     */
    @Deprecated("Use extension instead", ReplaceWith("observable.subscribeBy(onNext, onComplete, onError)"))
    protected fun <T> subscribe(observable: Observable<T>,
                                onNext: (T) -> Unit,
                                onComplete: () -> Unit,
                                onError: (Throwable) -> Unit): Disposable {

        return observable.subscribeBy(ObservableOperatorFreeze(freezeSelector), onNext, onComplete, onError)
    }

    @Deprecated("Use extension instead", ReplaceWith("single.subscribeBy(onSuccess, onError)"))
    protected fun <T> subscribe(single: Single<T>,
                                onSuccess: (T) -> Unit,
                                onError: (Throwable) -> Unit): Disposable {

        return single.subscribeBy(SingleOperatorFreeze(freezeSelector), onSuccess, onError)
    }

    @Deprecated("Use extension instead", ReplaceWith("completable.subscribeBy(onComplete, onError)"))
    protected fun subscribe(completable: Completable,
                            onComplete: () -> Unit,
                            onError: (Throwable) -> Unit): Disposable {

        return completable.subscribeBy(CompletableOperatorFreeze(freezeSelector), onComplete, onError)
    }

    @Deprecated("Use extension instead", ReplaceWith("maybe.subscribeBy(onSuccess, onComplete, onError)"))
    protected fun <T> subscribe(maybe: Maybe<T>,
                                onSuccess: (T) -> Unit,
                                onComplete: () -> Unit,
                                onError: (Throwable) -> Unit): Disposable {

        return maybe.subscribeBy(MaybeOperatorFreeze(freezeSelector), onSuccess, onComplete, onError)
    }


    /**
     * @see @link .subscribeWithoutFreezing
     */
    @Deprecated("Use extension instead", ReplaceWith("observable.subscribeBy(onNext, onError)"))
    protected fun <T> subscribeWithoutFreezing(observable: Observable<T>,
                                               onNext: (T) -> Unit,
                                               onError: (Throwable) -> Unit): Disposable {
        return observable.subscribeWithoutFreezingBy<T>(LambdaObserver(onNext.asConsumerSafe(), onError.asErrorConsumerSafe(),
                Functions.EMPTY_ACTION, Functions.emptyConsumer()))
    }

    @Deprecated("Use extension instead", ReplaceWith("single.subscribeBy(onSuccess, onError)"))
    protected fun <T> subscribeWithoutFreezing(single: Single<T>,
                                               onSuccess: (T) -> Unit,
                                               onError: (Throwable) -> Unit): Disposable {
        return single.subscribeWithoutFreezingBy(object : DisposableSingleObserver<T>() {
            override fun onSuccess(t: T) {
                onSuccess.invoke(t)
            }

            override fun onError(e: Throwable) {
                onError.invoke(e)
            }
        })
    }

    @Deprecated("Use extension instead", ReplaceWith("completable.subscribeBy(onComplete, onError)"))
    protected fun subscribeWithoutFreezing(completable: Completable,
                                           onComplete: () -> Unit,
                                           onError: (Throwable) -> Unit): Disposable {
        return completable.subscribeWithoutFreezingBy(object : DisposableCompletableObserver() {
            override fun onComplete() {
                onComplete.invoke()
            }

            override fun onError(e: Throwable) {
                onError.invoke(e)
            }
        })
    }

    @Deprecated("Use extension instead", ReplaceWith("maybe.subscribeBy(onSuccess, onComplete, onError)"))
    protected fun <T> subscribeWithoutFreezing(maybe: Maybe<T>,
                                               onSuccess: (T) -> Unit,
                                               onComplete: () -> Unit,
                                               onError: (Throwable) -> Unit): Disposable {
        return maybe.subscribeWithoutFreezingBy(object : DisposableMaybeObserver<T>() {

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
}