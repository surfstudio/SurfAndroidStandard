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
package ru.surfstudio.android.core.mvp.presenter;

import androidx.annotation.CallSuper;

import com.agna.ferro.rx.CompletableOperatorFreeze;
import com.agna.ferro.rx.MaybeOperatorFreeze;
import com.agna.ferro.rx.ObservableOperatorFreeze;
import com.agna.ferro.rx.SingleOperatorFreeze;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.functions.Functions;
import io.reactivex.internal.observers.LambdaObserver;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableMaybeObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.subjects.BehaviorSubject;
import ru.surfstudio.android.core.mvp.view.CoreView;
import ru.surfstudio.android.core.ui.event.ScreenEventDelegateManager;
import ru.surfstudio.android.core.ui.state.ScreenState;
import ru.surfstudio.android.rx.extension.ActionSafe;
import ru.surfstudio.android.rx.extension.BiFunctionSafe;
import ru.surfstudio.android.rx.extension.ConsumerSafe;
import ru.surfstudio.android.rx.extension.ObservableUtil;

/**
 * базовый класс презентера, содержащий всю корневую логику
 * Методы {@link #subscribe} добавляют логику замораживания
 * Rx событий на время пересоздания вью или когда экран находится в фоне, см {@link ObservableOperatorFreeze}
 * Также все подписки освобождаются при полном уничтожении экрана
 *
 * @param <V>
 */
public abstract class CorePresenter<V extends CoreView> { //todo детально все просмотреть

    private final CompositeDisposable disposables = new CompositeDisposable();
    private final BehaviorSubject<Boolean> freezeSelector = BehaviorSubject.createDefault(false);
    private V view;
    private boolean freezeEventsOnPause = true;

    public CorePresenter(ScreenEventDelegateManager eventDelegateManager, ScreenState screenState) {
        eventDelegateManager.registerDelegate(new CorePresenterGateway(this, screenState));
    }

    public void attachView(V view) {
        this.view = view;
    }

    /**
     * @return screen's view
     */
    protected V getView() {
        return view;
    }

    /**
     * This method is called, when view is ready
     *
     * @param viewRecreated - showSimpleDialog whether view created in first time or recreated after
     *                      changing configuration
     */
    public void onLoad(boolean viewRecreated) {
    }

    /**
     * вызывается при первом запуске экрана, если экран восстановлен с диска,
     * то это тоже считается первым запуском
     */
    public void onFirstLoad() {
    }

    /**
     * Called when view is started
     */
    public void onStart() {

    }

    /**
     * Called when view is resumed
     */
    @CallSuper
    public void onResume() {
        freezeSelector.onNext(false);
    }

    /**
     * Called when view is paused
     */
    public void onPause() {
        if (freezeEventsOnPause) {
            freezeSelector.onNext(true);
        }
    }

    /**
     * Called when view is stopped
     */
    public void onStop() {

    }

    public final void detachView() {
        onViewDetach();
        view = null;
    }

    /**
     * Called when view is detached
     */
    @CallSuper
    protected void onViewDetach() {
        freezeSelector.onNext(true);
    }

    /**
     * Called when screen is finally destroyed
     */
    @CallSuper
    public void onDestroy() {
        disposables.dispose();
    }

    /**
     * см {@link StateRestorer}
     *
     * @return
     */
    protected StateRestorer getStateRestorer() {
        return null;
    }

    /**
     * If true, all rx event would be frozen when screen paused, and unfrozen when screen resumed,
     * otherwise event would be frozen when {@link #onViewDetach()} called.
     * Default enabled.
     *
     * @param enabled
     */
    public void setFreezeOnPauseEnabled(boolean enabled) {
        this.freezeEventsOnPause = enabled;
    }

    //region subscribe

    /**
     * Apply {@link ObservableOperatorFreeze} and subscribe subscriber to the observable.
     * When screen finally destroyed, all subscriptions would be automatically unsubscribed.
     * For more information see description of this class.
     *
     * @return subscription
     */
    protected <T> Disposable subscribe(final Observable<T> observable,
                                       final ObservableOperatorFreeze<T> operator,
                                       final LambdaObserver<T> observer) {
        Disposable disposable = observable
                .lift(operator)
                .subscribeWith(observer);
        disposables.add(disposable);
        return disposable;
    }

    protected <T> Disposable subscribe(final Single<T> single,
                                       final SingleOperatorFreeze<T> operator,
                                       final DisposableSingleObserver<T> observer) {

        Disposable disposable = single
                .lift(operator)
                .subscribeWith(observer);
        disposables.add(disposable);
        return disposable;
    }

    protected Disposable subscribe(final Completable completable,
                                   final CompletableOperatorFreeze operator,
                                   final DisposableCompletableObserver observer) {
        Disposable disposable = completable
                .lift(operator)
                .subscribeWith(observer);
        disposables.add(disposable);
        return disposable;
    }

    protected <T> Disposable subscribe(final Maybe<T> maybe,
                                       final MaybeOperatorFreeze<T> operator,
                                       final DisposableMaybeObserver<T> observer) {
        Disposable disposable = maybe
                .lift(operator)
                .subscribeWith(observer);
        disposables.add(disposable);
        return disposable;
    }
    //endregion

    /**
     * @see #subscribe(Observable, ObservableOperatorFreeze, LambdaObserver)
     */
    protected <T> Disposable subscribe(final Observable<T> observable,
                                       final ObservableOperatorFreeze<T> operator,
                                       final ConsumerSafe<T> onNext,
                                       final ConsumerSafe<Throwable> onError) {
        return subscribe(observable, operator, onNext, ObservableUtil.EMPTY_ACTION, onError);
    }

    /**
     * @see #subscribe(Observable, ObservableOperatorFreeze, LambdaObserver)
     */
    protected <T> Disposable subscribe(final Observable<T> observable,
                                       final ObservableOperatorFreeze<T> operator,
                                       final ConsumerSafe<T> onNext,
                                       final ActionSafe onComplete,
                                       final ConsumerSafe<Throwable> onError) {
        return subscribe(observable, operator, new LambdaObserver<>(onNext, onError, onComplete, Functions.emptyConsumer()));
    }

    protected <T> Disposable subscribe(final Single<T> single,
                                       final SingleOperatorFreeze<T> operator,
                                       final ConsumerSafe<T> onSuccess,
                                       final ConsumerSafe<Throwable> onError) {
        return subscribe(single, operator, new DisposableSingleObserver<T>() {
            @Override
            public void onSuccess(T t) {
                onSuccess.accept(t);
            }

            @Override
            public void onError(Throwable e) {
                onError.accept(e);
            }
        });
    }

    protected <T> Disposable subscribe(final Maybe<T> maybe,
                                       final MaybeOperatorFreeze<T> operator,
                                       final ConsumerSafe<T> onSuccess,
                                       final ActionSafe onComplete,
                                       final ConsumerSafe<Throwable> onError) {
        return subscribe(maybe, operator, new DisposableMaybeObserver<T>() {
            @Override
            public void onSuccess(T t) {
                onSuccess.accept(t);
            }

            @Override
            public void onError(Throwable e) {
                onError.accept(e);
            }

            @Override
            public void onComplete() {
                onComplete.run();
            }
        });
    }


    protected Disposable subscribe(final Completable completable,
                                   final CompletableOperatorFreeze operator,
                                   final ActionSafe onComplete,
                                   final ConsumerSafe<Throwable> onError) {
        return subscribe(completable, operator, new DisposableCompletableObserver() {
            @Override
            public void onComplete() {
                onComplete.run();
            }

            @Override
            public void onError(Throwable e) {
                onError.accept(e);
            }
        });
    }

    /**
     * @param replaceFrozenEventPredicate - used for reduce num element in freeze buffer
     * @see #subscribe(Observable, ObservableOperatorFreeze, LambdaObserver)
     * @see ObservableOperatorFreeze
     */
    protected <T> Disposable subscribe(final Observable<T> observable,
                                       final BiFunctionSafe<T, T, Boolean> replaceFrozenEventPredicate,
                                       final LambdaObserver<T> observer) {

        return subscribe(observable, createOperatorFreeze(replaceFrozenEventPredicate), observer);
    }


    /**
     * @param replaceFrozenEventPredicate - used for reduce num element in freeze buffer
     * @see @link #subscribe(Observable, OperatorFreeze, Subscriber)
     * @see @link OperatorFreeze
     */
    protected <T> Disposable subscribe(final Observable<T> observable,
                                       final BiFunctionSafe<T, T, Boolean> replaceFrozenEventPredicate,
                                       final ConsumerSafe<T> onNext,
                                       final ConsumerSafe<Throwable> onError) {

        return subscribe(observable, createOperatorFreeze(replaceFrozenEventPredicate), onNext, onError);
    }

    /**
     * @see @link #subscribe(Observable, OperatorFreeze, Subscriber)
     */
    protected <T> Disposable subscribe(final Observable<T> observable,
                                       final LambdaObserver<T> observer) {

        return subscribe(observable, this.createOperatorFreeze(), observer);
    }

    /**
     * @see @link #subscribe(Observable, OperatorFreeze, Subscriber)
     */
    protected <T> Disposable subscribe(final Observable<T> observable,
                                       final ConsumerSafe<T> onNext) {

        return subscribe(observable, this.createOperatorFreeze(), onNext, ObservableUtil.ON_ERROR_MISSING);
    }


    /**
     * @see @link #subscribe(Observable, OperatorFreeze, Subscriber)
     */
    protected <T> Disposable subscribe(final Observable<T> observable,
                                       final ConsumerSafe<T> onNext,
                                       final ConsumerSafe<Throwable> onError) {

        return subscribe(observable, onNext, ObservableUtil.EMPTY_ACTION, onError);
    }


    /**
     * @see @link #subscribe(Observable, OperatorFreeze, Subscriber)
     */
    protected <T> Disposable subscribe(final Observable<T> observable,
                                       final ConsumerSafe<T> onNext,
                                       final ActionSafe onComplete,
                                       final ConsumerSafe<Throwable> onError) {

        return subscribe(observable, this.createOperatorFreeze(), onNext, onComplete, onError);
    }

    protected <T> Disposable subscribe(final Single<T> single,
                                       final ConsumerSafe<T> onNext) {
        return subscribe(single, this.createSingleOperatorFreeze(), onNext, ObservableUtil.ON_ERROR_MISSING);
    }

    protected <T> Disposable subscribe(final Single<T> single,
                                       final ConsumerSafe<T> onSuccess,
                                       final ConsumerSafe<Throwable> onError) {

        return subscribe(single, this.createSingleOperatorFreeze(), onSuccess, onError);
    }

    protected Disposable subscribe(final Completable completable,
                                   final ActionSafe onComplete,
                                   final ConsumerSafe<Throwable> onError) {

        return subscribe(completable, new CompletableOperatorFreeze(freezeSelector), onComplete, onError);
    }


    protected <T> Disposable subscribe(final Maybe<T> maybe,
                                       final ConsumerSafe<T> onSuccess,
                                       final ActionSafe onComplete,
                                       final ConsumerSafe<Throwable> onError) {

        return subscribe(maybe, new MaybeOperatorFreeze<>(freezeSelector), onSuccess, onComplete, onError);
    }

    //region subscribeTakeLastFrozen

    /**
     * Subscribe and take only last emitted value from frozen predicate.
     * This is very useful in situations when your screen is long time inactive and it should react
     * only to last emitted value.
     *
     * For example, to prevent saving all the copies of data in storage when screen becomes visible,
     * and save only last emitted copy instead.
     *
     * @param observable observable to subscribe
     * @param onNext     action to call when new portion of data is emitted
     * @param onError    action to call when the error is occurred
     * @param <T>        type of observable element
     * @return Disposable
     */
    protected <T> Disposable subscribeTakeLastFrozen(
            Observable<T> observable,
            ConsumerSafe<T> onNext,
            ConsumerSafe<Throwable> onError
    ) {
        return subscribe(observable, createTakeLastFrozenPredicate(), onNext, onError);
    }

    /**
     * Subscribe and take only last emitted value from frozen predicate.
     * This is very useful in situations when your screen is long time inactive and it should react
     * only to last emitted value.
     *
     * For example, to prevent saving all the copies of data in storage when screen becomes visible,
     * and save only last emitted copy instead.
     *
     * @param observable observable to subscribe
     * @param onNext     action to call when new portion of data is emitted
     * @param <T>        type of observable element
     * @return Disposable
     */
    protected <T> Disposable subscribeTakeLastFrozen(
            Observable<T> observable,
            ConsumerSafe<T> onNext
    ) {
        return subscribe(observable, createTakeLastFrozenPredicate(), onNext, ObservableUtil.ON_ERROR_MISSING);
    }

    /**
     * Subscribe and take only last emitted value from frozen predicate.
     * This is very useful in situations when your screen is long time inactive and it should react
     * only to last emitted value.
     *
     * For example, to prevent saving all the copies of data in storage when screen becomes visible,
     * and save only last emitted copy instead.
     *
     * @param observable observable to subscribe
     * @param observer observer that receives data
     * @param <T>        type of observable element
     * @return Disposable
     */
    protected <T> Disposable subscribeTakeLastFrozen(
            Observable<T> observable,
            LambdaObserver<T> observer
    ) {
        return subscribe(observable, createTakeLastFrozenPredicate(), observer);
    }

    //endregion

    /**
     * Subscribe subscriber to the observable without applying {@link ObservableOperatorFreeze}
     * When screen finally destroyed, all subscriptions would be automatically unsubscribed.
     *
     * @return subscription
     */
    protected <T> Disposable subscribeWithoutFreezing(final Observable<T> observable,
                                                      final LambdaObserver<T> subscriber) {

        Disposable disposable = observable.subscribeWith(subscriber);
        disposables.add(disposable);
        return disposable;
    }

    protected <T> Disposable subscribeWithoutFreezing(final Single<T> single,
                                                      final DisposableSingleObserver<T> subscriber) {

        Disposable disposable = single.subscribeWith(subscriber);
        disposables.add(disposable);
        return disposable;
    }

    protected Disposable subscribeWithoutFreezing(final Completable completable,
                                                  final DisposableCompletableObserver subscriber) {

        Disposable disposable = completable.subscribeWith(subscriber);
        disposables.add(disposable);
        return disposable;
    }

    protected <T> Disposable subscribeWithoutFreezing(final Maybe<T> maybe,
                                                      final DisposableMaybeObserver<T> subscriber) {

        Disposable disposable = maybe.subscribeWith(subscriber);
        disposables.add(disposable);
        return disposable;
    }

    /**
     * @see @link #subscribeWithoutFreezing(Observable, Subscriber)
     */
    protected <T> Disposable subscribeWithoutFreezing(final Observable<T> observable,
                                                      final ConsumerSafe<T> onNext,
                                                      final ConsumerSafe<Throwable> onError) {
        return subscribeWithoutFreezing(observable, new LambdaObserver<>(onNext, onError,
                Functions.EMPTY_ACTION, Functions.emptyConsumer()));
    }

    protected <T> Disposable subscribeWithoutFreezing(final Single<T> single,
                                                      final ConsumerSafe<T> onSuccess,
                                                      final ConsumerSafe<Throwable> onError) {
        return subscribeWithoutFreezing(single, new DisposableSingleObserver<T>() {
            @Override
            public void onSuccess(T t) {
                onSuccess.accept(t);
            }

            @Override
            public void onError(Throwable e) {
                onError.accept(e);
            }
        });
    }

    protected Disposable subscribeWithoutFreezing(final Completable completable,
                                                  final ActionSafe onComplete,
                                                  final ConsumerSafe<Throwable> onError) {
        return subscribeWithoutFreezing(completable, new DisposableCompletableObserver() {
            @Override
            public void onComplete() {
                onComplete.run();
            }

            @Override
            public void onError(Throwable e) {
                onError.accept(e);
            }
        });
    }

    protected <T> Disposable subscribeWithoutFreezing(final Maybe<T> maybe,
                                                      final ConsumerSafe<T> onSuccess,
                                                      final ActionSafe onComplete,
                                                      final ConsumerSafe<Throwable> onError) {
        return subscribeWithoutFreezing(maybe, new DisposableMaybeObserver<T>() {

            @Override
            public void onSuccess(T t) {
                onSuccess.accept(t);
            }

            @Override
            public void onComplete() {
                onComplete.run();
            }

            @Override
            public void onError(Throwable e) {
                onError.accept(e);
            }
        });
    }

    protected <T> ObservableOperatorFreeze<T> createOperatorFreeze(BiFunctionSafe<T, T, Boolean> replaceFrozenEventPredicate) {
        return new ObservableOperatorFreeze<>(freezeSelector, replaceFrozenEventPredicate);
    }

    protected <T> ObservableOperatorFreeze<T> createOperatorFreeze() {
        return new ObservableOperatorFreeze<>(freezeSelector);
    }

    protected <T> SingleOperatorFreeze<T> createSingleOperatorFreeze() {
        return new SingleOperatorFreeze<>(freezeSelector);
    }

    /**
     * Predicate that takes only the last emitted value from the frozen buffer
     *
     * @param <T> observable element type
     * @return predicate
     * @see com.agna.ferro.rx.ObservableOperatorFreeze
     */
    protected <T> BiFunctionSafe<T, T, Boolean> createTakeLastFrozenPredicate() {
        return new BiFunctionSafe<T, T, Boolean>() {
            @Override
            public Boolean apply(T t, T t2) {
                return true;
            }
        };
    }

    protected boolean isDisposableInactive(Disposable disposable) {
        return disposable == null || disposable.isDisposed();
    }

    protected boolean isDisposableActive(Disposable disposable) {
        return disposable != null && !disposable.isDisposed();
    }

}