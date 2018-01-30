package ru.surfstudio.android.core.ui.base.screen.presenter;


import android.support.annotation.CallSuper;

import com.agna.ferro.rx.ObservableOperatorFreeze;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.functions.Functions;
import io.reactivex.internal.observers.LambdaObserver;
import io.reactivex.subjects.BehaviorSubject;
import ru.surfstudio.android.core.ui.base.event.delegate.ScreenEventDelegateManager;
import ru.surfstudio.android.core.ui.base.screen.state.ScreenState;
import ru.surfstudio.android.core.ui.base.screen.view.core.CoreView;
import ru.surfstudio.android.core.util.rx.ObservableUtil;
import ru.surfstudio.android.core.util.rx.SafeAction;
import ru.surfstudio.android.core.util.rx.SafeBiFunction;
import ru.surfstudio.android.core.util.rx.SafeConsumer;

/**
 * базовый класс презентера, содержащий всю корневую логику
 * Методы {@link #subscribe} добавляют логику замораживания
 * Rx событий на время пересоздания вью или когда экран находится в фоне, см {@link ObservableOperatorFreeze}
 * Также все подписки освобождаются при полном уничтожении экрана
 *
 * @param <V>
 */
public abstract class CorePresenter<V extends CoreView> {

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
     * @param viewRecreated - show whether view created in first time or recreated after
     *                      changing configuration
     */
    public void onLoad(boolean viewRecreated) {
    }

    /**
     *  //todo
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
        view = null;
        onViewDetached();
    }

    /**
     * Called when view is detached
     */
    @CallSuper
    protected void onViewDetached() {
        freezeSelector.onNext(true);
    }

    /**
     * Called when screen is finally destroyed
     */
    @CallSuper
    public void onDestroy() {
        disposables.dispose();
    }

    //todo коммент
    protected StateRestorer getStateRestorer() {
        return null;
    }

    /**
     * If true, all rx event would be frozen when screen paused, and unfrozen when screen resumed,
     * otherwise event would be frozen when {@link #onViewDetached()} called.
     * Default enabled.
     *
     * @param enabled
     */
    public void setFreezeOnPauseEnabled(boolean enabled) {
        this.freezeEventsOnPause = enabled;
    }

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

    /**
     * @see #subscribe(Observable, ObservableOperatorFreeze, LambdaObserver)
     */
    protected <T> Disposable subscribe(final Observable<T> observable,
                                       final ObservableOperatorFreeze<T> operator,
                                       final SafeConsumer<T> onNext,
                                       final SafeConsumer<Throwable> onError) {
        return subscribe(observable, operator, onNext, ObservableUtil.EMPTY_ACTION, onError);
    }

    /**
     * @see #subscribe(Observable, ObservableOperatorFreeze, LambdaObserver)
     */
    protected <T> Disposable subscribe(final Observable<T> observable,
                                       final ObservableOperatorFreeze<T> operator,
                                       final SafeConsumer<T> onNext,
                                       final SafeAction onComplete,
                                       final SafeConsumer<Throwable> onError) {
        return subscribe(observable, operator, new LambdaObserver<>(onNext, onError, onComplete, Functions.emptyConsumer()));
    }

    /**
     * @param replaceFrozenEventPredicate - used for reduce num element in freeze buffer
     * @see #subscribe(Observable, ObservableOperatorFreeze, LambdaObserver)
     * @see ObservableOperatorFreeze
     */
    protected <T> Disposable subscribe(final Observable<T> observable,
                                       final SafeBiFunction<T, T, Boolean> replaceFrozenEventPredicate,
                                       final LambdaObserver<T> observer) {

        return subscribe(observable, createOperatorFreeze(replaceFrozenEventPredicate), observer);
    }

    /**
     * @param replaceFrozenEventPredicate - used for reduce num element in freeze buffer
     * @see @link #subscribe(Observable, OperatorFreeze, Subscriber)
     * @see @link OperatorFreeze
     */
    protected <T> Disposable subscribe(final Observable<T> observable,
                                       final SafeBiFunction<T, T, Boolean> replaceFrozenEventPredicate,
                                       final SafeConsumer<T> onNext,
                                       final SafeConsumer<Throwable> onError) {

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
                                       final SafeConsumer<T> onNext) {

        return subscribe(observable, this.createOperatorFreeze(), onNext, ObservableUtil.ON_ERROR_MISSING);
    }


    /**
     * @see @link #subscribe(Observable, OperatorFreeze, Subscriber)
     */
    protected <T> Disposable subscribe(final Observable<T> observable,
                                       final SafeConsumer<T> onNext,
                                       final SafeConsumer<Throwable> onError) {

        return subscribe(observable, onNext, ObservableUtil.EMPTY_ACTION, onError);
    }

    /**
     * @see @link #subscribe(Observable, OperatorFreeze, Subscriber)
     */
    protected <T> Disposable subscribe(final Observable<T> observable,
                                       final SafeConsumer<T> onNext,
                                       final SafeAction onComplete,
                                       final SafeConsumer<Throwable> onError) {

        return subscribe(observable, this.createOperatorFreeze(), onNext, onComplete, onError);
    }


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

    /**
     * @see @link #subscribeWithoutFreezing(Observable, Subscriber)
     */
    protected <T> Disposable subscribeWithoutFreezing(final Observable<T> observable,
                                                      final SafeConsumer<T> onNext,
                                                      final SafeConsumer<Throwable> onError) {
        return subscribeWithoutFreezing(observable, new LambdaObserver<>(onNext, onError,
                Functions.EMPTY_ACTION, Functions.emptyConsumer()));
    }


    protected <T> ObservableOperatorFreeze<T> createOperatorFreeze(SafeBiFunction<T, T, Boolean> replaceFrozenEventPredicate) {
        return new ObservableOperatorFreeze<>(freezeSelector, replaceFrozenEventPredicate);
    }

    protected <T> ObservableOperatorFreeze<T> createOperatorFreeze() {
        return new ObservableOperatorFreeze<>(freezeSelector);
    }

    protected boolean isDisposableInactive(Disposable disposable) {
        return disposable == null || disposable.isDisposed();
    }

    protected boolean isDisposableActive(Disposable disposable) {
        return disposable != null && !disposable.isDisposed();
    }

}