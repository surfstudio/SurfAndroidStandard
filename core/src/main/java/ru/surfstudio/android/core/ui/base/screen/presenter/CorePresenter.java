package ru.surfstudio.android.core.ui.base.screen.presenter;


import android.support.annotation.CallSuper;

import com.agna.ferro.rx.OperatorFreeze;

import ru.surfstudio.android.core.ui.base.event.delegate.ScreenEventDelegateManager;
import ru.surfstudio.android.core.ui.base.scope.PersistentScope;
import ru.surfstudio.android.core.ui.base.screen.view.core.CoreView;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func2;
import rx.internal.util.InternalObservableUtils;
import rx.subjects.BehaviorSubject;
import rx.subscriptions.CompositeSubscription;

/**
 * базовый класс презентера, содержащий всю корневую логику
 * Методы {@link #subscribe} добавляют логику замораживания
 * Rx событий на время пересоздания вью или когда экран находится в фоне, см {@link OperatorFreeze}
 * Также все подписки освобождаются при полном уничтожении экрана
 * @param <V>
 */
public abstract class CorePresenter<V extends CoreView> {

    private V view;
    private final CompositeSubscription subscriptions = new CompositeSubscription();
    private final BehaviorSubject<Boolean> freezeSelector = BehaviorSubject.create(false);
    private boolean freezeEventsOnPause = true;

    public CorePresenter(ScreenEventDelegateManager delegateManager, PersistentScope persistentScope) {
        delegateManager.registerDelegate(new CorePresenterGateway(this, persistentScope));
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

    //todo comment
    protected void onFirstLoad() {

    }

    /**
     * This method is called, when view is ready
     * @param viewRecreated - show whether view created in first time or recreated after
     *                        changing configuration
     */
    @CallSuper
    protected void onLoad(boolean viewRecreated) {

    }

    /**
     * Called after {@link this#onLoad}
     */
    protected void onLoadFinished() {
    }

    /**
     * Called when view is started
     */
    protected void onStart(){

    }

    /**
     * Called when view is resumed
     */
    @CallSuper
    protected void onResume(){
        freezeSelector.onNext(false);
    }

    /**
     * Called when view is paused
     */
    protected void onPause(){
        if(freezeEventsOnPause) {
            freezeSelector.onNext(true);
        }
    }

    /**
     * Called when view is stopped
     */
    protected void onStop(){

    }

    /**
     * Called when view is detached
     */
    @CallSuper
    protected void onViewDetached() {
        view = null;
        freezeSelector.onNext(true);
    }

    /**
     * Called when screen is finally destroyed
     */
    @CallSuper
    protected void onDestroy() {
        subscriptions.unsubscribe();
    }

    //todo коммент
    protected StateRestorer getStateRestorer(){
        return null;
    }

    /**
     * If true, all rx event would be frozen when screen paused, and unfrozen when screen resumed,
     * otherwise event would be frozen when {@link #onViewDetached()} called.
     * Default enabled.
     * @param enabled
     */
    public void setFreezeOnPauseEnabled(boolean enabled) {
        this.freezeEventsOnPause = enabled;
    }

    /**
     * Apply {@link OperatorFreeze} and subscribe subscriber to the observable.
     * When screen finally destroyed, all subscriptions would be automatically unsubscribed.
     * For more information see description of this class.
     * @return subscription
     */
    protected <T> Subscription subscribe(final Observable<T> observable,
                                       final OperatorFreeze<T> operator,
                                       final Subscriber<T> subscriber) {
        Subscription subscription = observable
                .lift(operator)
                .subscribe(subscriber);
        subscriptions.add(subscription);
        return subscription;
    }

    /**
     * @see #subscribe(Observable, OperatorFreeze, Subscriber)
     */
    protected <T> Subscription subscribe(final Observable<T> observable,
                                       final OperatorFreeze<T> operator,
                                       final Action1<T> onNext,
                                       final Action1<Throwable> onError) {
        return subscribe(observable, operator,
                new Subscriber<T>() {
                    @Override
                    public void onCompleted() {
                        // do nothing
                    }

                    @Override
                    public void onError(Throwable e) {
                        onError.call(e);
                    }

                    @Override
                    public void onNext(T t) {
                        onNext.call(t);
                    }
                });
    }

    /**
     * @see #subscribe(Observable, OperatorFreeze, Subscriber)
     * @param replaceFrozenEventPredicate - used for reduce num element in freeze buffer
     *                                    @see OperatorFreeze
     */
    protected <T> Subscription subscribe(final Observable<T> observable,
                                         final Func2<T, T, Boolean> replaceFrozenEventPredicate,
                                         final Subscriber<T> subscriber) {

        return subscribe(observable, createOperatorFreeze(replaceFrozenEventPredicate), subscriber);
    }

    /**
     * @see @link #subscribe(Observable, OperatorFreeze, Subscriber)
     * @param replaceFrozenEventPredicate - used for reduce num element in freeze buffer
     *                                    @see @link OperatorFreeze
     */
    protected <T> Subscription subscribe(final Observable<T> observable,
                                         final Func2<T, T, Boolean> replaceFrozenEventPredicate,
                                         final Action1<T> onNext,
                                         final Action1<Throwable> onError) {

        return subscribe(observable, createOperatorFreeze(replaceFrozenEventPredicate), onNext, onError);
    }

    /**
     * @see @link #subscribe(Observable, OperatorFreeze, Subscriber)
     */
    protected <T> Subscription subscribe(final Observable<T> observable,
                                         final Subscriber<T> subscriber) {

        return subscribe(observable, this.<T>createOperatorFreeze(), subscriber);
    }

    /**
     * @see @link #subscribe(Observable, OperatorFreeze, Subscriber)
     */
    protected <T> Subscription subscribe(final Observable<T> observable,
                                         final Action1<T> onNext) {

        return subscribe(observable, this.<T>createOperatorFreeze(), onNext, InternalObservableUtils.ERROR_NOT_IMPLEMENTED); //todo crash
    }


    /**
     * @see @link #subscribe(Observable, OperatorFreeze, Subscriber)
     */
    protected <T> Subscription subscribe(final Observable<T> observable,
                                         final Action1<T> onNext,
                                         final Action1<Throwable> onError) {

        return subscribe(observable, this.<T>createOperatorFreeze(), onNext, onError);
    }

    /**
     * Subscribe subscriber to the observable without applying {@link OperatorFreeze}
     * When screen finally destroyed, all subscriptions would be automatically unsubscribed.
     * @return subscription
     */
    protected <T> Subscription subscribeWithoutFreezing(final Observable<T> observable,
                                                        final Subscriber<T> subscriber) {

        Subscription subscription = observable
                .subscribe(subscriber);
        subscriptions.add(subscription);
        return subscription;
    }

    /**
     * @see @link #subscribeWithoutFreezing(Observable, Subscriber)
     */
    protected <T> Subscription subscribeWithoutFreezing(final Observable<T> observable,
                                                        final Action1<T> onNext,
                                                        final Action1<Throwable> onError) {

        return subscribeWithoutFreezing(observable, new Subscriber<T>() {
            @Override
            public void onCompleted() {
                // do nothing
            }

            @Override
            public void onError(Throwable e) {
                onError.call(e);
            }

            @Override
            public void onNext(T t) {
                onNext.call(t);
            }
        });
    }


    protected <T> OperatorFreeze<T> createOperatorFreeze(Func2<T, T, Boolean> replaceFrozenEventPredicate) {
        return new OperatorFreeze<>(freezeSelector, replaceFrozenEventPredicate);
    }

    protected <T> OperatorFreeze<T> createOperatorFreeze() {
        return new OperatorFreeze<>(freezeSelector);
    }

    protected boolean isInactive(Subscription subscription) {
        return subscription == null || subscription.isUnsubscribed();
    }

    protected boolean isActive(Subscription subscription) {
        return !isInactive(subscription);
    }


}
