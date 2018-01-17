package ru.surfstudio.android.core.ui.base.screen.presenter;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.agna.ferro.rx.OperatorFreeze;

import ru.surfstudio.android.core.app.connection.ConnectionProvider;
import ru.surfstudio.android.core.app.scheduler.SchedulersProvider;
import ru.surfstudio.android.core.ui.base.navigation.activity.navigator.ActivityNavigator;
import ru.surfstudio.android.core.ui.base.screen.view.HandleableErrorView;
import ru.surfstudio.android.core.ui.base.screen.view.core.CoreView;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;

/**
 * базовый класс презентра для приложения
 * Подписки через все виды методов {@link #subscribe}, {@link #subscribeWithoutFreezing},
 * {@link #subscribeIoHandleError} обрабатываются в главном потоке
 * При подписке с помощью методов {@link #subscribeIoHandleError} observable источника переводится в
 * background поток.
 * Кроме того {@link #subscribeIoHandleError} содержит стандартную обработку ошибок
 * <p>
 * Кроме того содержит метод {@link #finish()} для закрытия текущего экрана, в дефолтной
 * имплементации закрывает активити
 * <p>
 * см {@link CorePresenter}
 *
 * @param <V>
 */
public abstract class BasePresenter<V extends CoreView & HandleableErrorView> extends CorePresenter<V> {

    private final ActivityNavigator activityNavigator;
    private final SchedulersProvider schedulersProvider;
    private final ConnectionProvider connectionProvider;
    private Subscription autoReloadSubscription;

    public BasePresenter(BasePresenterDependency basePresenterDependency) {
        super(basePresenterDependency.getDelegateManagerProvider(), basePresenterDependency.getScreenEventDelegates());
        this.schedulersProvider = basePresenterDependency.getSchedulersProvider();
        this.activityNavigator = basePresenterDependency.getActivityNavigator();
        this.connectionProvider = basePresenterDependency.getConnectionProvider();
    }

    /**
     * закрывает текущую активити
     * если необходима другая логика закрытия экрана, следует переопределить этот метод
     */
    public void finish() {
        activityNavigator.finishCurrent();
    }

    /**
     * Закрывает все Affinity Activity.
     */
    public void finishAffinity() {
        activityNavigator.finishAffinity();
    }

    @Override
    protected <T> Subscription subscribe(final Observable<T> observable,
                                         final OperatorFreeze<T> operator,
                                         final Subscriber<T> subscriber) {
        return super.subscribe(observable.observeOn(schedulersProvider.main(), true), operator, subscriber);
    }

    @Override
    protected <T> Subscription subscribeWithoutFreezing(final Observable<T> observable,
                                                        final Subscriber<T> subscriber) {
        return super.subscribe(observable.observeOn(schedulersProvider.main(), true), subscriber);
    }

    /**
     * Работает также как {@link #subscribe}, кроме того автоматически обрабатывает ошибки,
     * см {@link HandleableErrorView} и переводит выполенения потока в фон
     */
    protected <T> Subscription subscribeIoHandleError(Observable<T> observable,
                                                      final Action1<T> onNext) {
        return subscribeIoHandleError(observable, onNext, null);
    }

    /**
     * Работает также как {@link #subscribe}, кроме того автоматически обрабатывает ошибки,
     * см {@link HandleableErrorView} и переводит выполенения потока в фон
     */
    protected <T> Subscription subscribeIoHandleError(Observable<T> observable,
                                                      final Action1<T> onNext,
                                                      final Action1<Throwable> onError) {
        observable = observable.subscribeOn(schedulersProvider.worker());
        return subscribe(observable, onNext, e -> handleError(e, onError));
    }

    /**
     * Работает также как {@link #subscribe}, кроме того автоматически обрабатывает ошибки,
     * см {@link HandleableErrorView} и переводит выполенения потока в фон
     */
    protected <T> Subscription subscribeIoHandleError(Observable<T> observable,
                                                      final Action1<T> onNext,
                                                      final Action0 onCompleted,
                                                      final Action1<Throwable> onError) {
        observable = observable.subscribeOn(schedulersProvider.worker());
        return subscribe(observable, onNext, onCompleted, e -> handleError(e, onError));
    }


    protected <T> Subscription subscribeIo(Observable<T> observable,
                                           final Action1<T> onNext,
                                           final Action1<Throwable> onError) {
        observable = observable.subscribeOn(schedulersProvider.worker());
        return subscribe(observable, onNext, onError);
    }

    /**
     * {@see subscribeIo}
     * автоматически вызовет autoReloadAction при появлении интернета если на момент выполнения
     * observable не было подключения к интернету
     */
    protected <T> Subscription subscribeIoAutoReload(Observable<T> observable,
                                                     final Action0 autoReloadAction,
                                                     final Action1<T> onNext,
                                                     final Action1<Throwable> onError) {
        return subscribe(initializeAutoReload(observable, autoReloadAction), onNext, onError);
    }

    /**
     * {@see subscribeIoAutoReload} кроме того автоматически обрабатывает ошибки
     */
    protected <T> Subscription subscribeIoHandleErrorAutoReload(Observable<T> observable,
                                                                final Action0 autoReloadAction,
                                                                final Action1<T> onNext,
                                                                @Nullable final Action1<Throwable> onError) {
        return subscribeIoHandleError(initializeAutoReload(observable, autoReloadAction), onNext, onError);
    }

    private void handleError(Throwable e, Action1<Throwable> onError) {
        getView().handleError(e);
        if (onError != null) {
            onError.call(e);
        }
    }

    private void cancelAutoReload() {
        if (isSubscriptionActive(autoReloadSubscription)) {
            autoReloadSubscription.unsubscribe();
        }
    }

    @NonNull
    private <T> Observable<T> initializeAutoReload(Observable<T> observable, Action0 reloadAction) {
        return observable.doOnError(e -> {
            cancelAutoReload();
            if (connectionProvider.isDisconnected()) {
                autoReloadSubscription = subscribe(connectionProvider.observeConnectionChanges()
                                .filter(connected -> connected)
                                .first(),
                        connected -> reloadAction.call());
            }
        });
    }
}
