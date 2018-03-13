package ru.surfstudio.android.core.mvp.presenter;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.agna.ferro.rx.ObservableOperatorFreeze;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.observers.LambdaObserver;
import ru.surfstudio.android.connection.ConnectionProvider;
import ru.surfstudio.android.core.mvp.error.ErrorHandler;
import ru.surfstudio.android.core.mvp.view.CoreView;
import ru.surfstudio.android.core.ui.navigation.activity.navigator.ActivityNavigator;
import ru.surfstudio.android.rx.extension.ActionSafe;
import ru.surfstudio.android.rx.extension.ConsumerSafe;
import ru.surfstudio.android.rx.extension.scheduler.SchedulersProvider;


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
 * Имеет методы subscribe c постфиксом AutoReload, в которые следует передать лямбду, которая будет
 * вызвана при появлении интернета, если во время запроса интернета не было. Только последний запрос
 * подписанный с помощью этих методов будет с таким свойством
 * см {@link CorePresenter}
 *
 * @param <V>
 */
public abstract class BasePresenter<V extends CoreView> extends CorePresenter<V> {

    private final ActivityNavigator activityNavigator;
    private final SchedulersProvider schedulersProvider;
    private final ConnectionProvider connectionProvider;
    private ErrorHandler errorHandler;
    private Disposable autoReloadDisposable;

    public BasePresenter(BasePresenterDependency basePresenterDependency) {
        super(basePresenterDependency.getEventDelegateManager(), basePresenterDependency.getScreenState());
        this.schedulersProvider = basePresenterDependency.getSchedulersProvider();
        this.activityNavigator = basePresenterDependency.getActivityNavigator();
        this.connectionProvider = basePresenterDependency.getConnectionProvider();
        this.errorHandler = basePresenterDependency.getErrorHandler();
    }

    @Override
    protected <T> Disposable subscribe(final Observable<T> observable,
                                       final ObservableOperatorFreeze<T> operator,
                                       final LambdaObserver<T> observer) {
        return super.subscribe(observable.observeOn(schedulersProvider.main(), true), operator, observer);
    }

    @Override
    protected <T> Disposable subscribeWithoutFreezing(final Observable<T> observable,
                                                      final LambdaObserver<T> observer) {
        return super.subscribe(observable.observeOn(schedulersProvider.main(), true), observer);
    }

    /**
     * закрывает текущую активити
     * если необходима другая логика закрытия экрана, следует переопределить этот метод
     */
    public void finish() {
        activityNavigator.finishCurrent();
    }

    /**
     * Устанавливает {@link ErrorHandler} вместо дефолтного
     */
    @NonNull
    public void setErrorHandler(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    protected <T> Disposable subscribe(final Single<T> single,
                                       final ObservableOperatorFreeze<T> operator,
                                       final LambdaObserver<T> observer) {
        return subscribe(single.toObservable().observeOn(schedulersProvider.main(), true), operator, observer);
    }

    protected <T> Disposable subscribeWithoutFreezing(final Single<T> single,
                                                      final LambdaObserver<T> observer) {
        return super.subscribe(single.toObservable().observeOn(schedulersProvider.main(), true), observer);
    }

    //region subscribeIoHandleError

    /**
     * Работает также как {@link #subscribe}, кроме того автоматически обрабатывает ошибки,
     * см {@link ErrorHandler} и переводит выполенения потока в фон
     */
    protected <T> Disposable subscribeIoHandleError(Observable<T> observable,
                                                    final ConsumerSafe<T> onNext) {
        return subscribeIoHandleError(observable, onNext, null);
    }

    protected <T> Disposable subscribeIoHandleError(Single<T> single,
                                                    final ConsumerSafe<T> onNext) {
        return subscribeIoHandleError(single.toObservable(), onNext, null);
    }

    protected <T> Disposable subscribeIoHandleError(Observable<T> observable,
                                                    final ConsumerSafe<T> onNext,
                                                    final ConsumerSafe<Throwable> onError) {
        observable = observable.subscribeOn(schedulersProvider.worker());
        return subscribe(observable, onNext, e -> handleError(e, onError));
    }

    protected <T> Disposable subscribeIoHandleError(Single<T> single,
                                                    final ConsumerSafe<T> onNext,
                                                    final ConsumerSafe<Throwable> onError) {
        single = single.subscribeOn(schedulersProvider.worker());
        return subscribe(single.toObservable(), onNext, e -> handleError(e, onError));
    }

    protected <T> Disposable subscribeIoHandleError(Observable<T> observable,
                                                    final ConsumerSafe<T> onNext,
                                                    final ActionSafe onComplete,
                                                    final ConsumerSafe<Throwable> onError) {
        observable = observable.subscribeOn(schedulersProvider.worker());
        return subscribe(observable, onNext, onComplete, e -> handleError(e, onError));
    }

    protected <T> Disposable subscribeIoHandleError(Single<T> single,
                                                    final ConsumerSafe<T> onNext,
                                                    final ActionSafe onComplete,
                                                    final ConsumerSafe<Throwable> onError) {
        single = single.subscribeOn(schedulersProvider.worker());
        return subscribe(single.toObservable(), onNext, onComplete, e -> handleError(e, onError));
    }
    //endregion

    //region subscribeIo
    protected <T> Disposable subscribeIo(Observable<T> observable,
                                         final ConsumerSafe<T> onNext,
                                         final ConsumerSafe<Throwable> onError) {
        observable = observable.subscribeOn(schedulersProvider.worker());
        return subscribe(observable, onNext, onError);
    }

    protected <T> Disposable subscribeIo(Single<T> single,
                                         final ConsumerSafe<T> onNext,
                                         final ConsumerSafe<Throwable> onError) {
        single = single.subscribeOn(schedulersProvider.worker());
        return subscribe(single.toObservable(), onNext, onError);
    }

    protected <T> Disposable subscribeIo(Observable<T> observable,
                                         final ConsumerSafe<T> onNext,
                                         final ActionSafe onComplete,
                                         final ConsumerSafe<Throwable> onError) {
        observable = observable.subscribeOn(schedulersProvider.worker());
        return subscribe(observable, onNext, onComplete, onError);
    }

    protected <T> Disposable subscribeIo(Single<T> single,
                                         final ConsumerSafe<T> onNext,
                                         final ActionSafe onComplete,
                                         final ConsumerSafe<Throwable> onError) {
        single = single.subscribeOn(schedulersProvider.worker());
        return subscribe(single.toObservable(), onNext, onComplete, onError);
    }
    //endregion

    //region subscribeIoAutoReload

    /**
     * {@see subscribeIo}
     * автоматически вызовет autoReloadAction при появлении интернета если на момент выполнения
     * observable не было подключения к интернету
     */
    protected <T> Disposable subscribeIoAutoReload(Observable<T> observable,
                                                   final ActionSafe autoReloadAction,
                                                   final ConsumerSafe<T> onNext,
                                                   final ConsumerSafe<Throwable> onError) {
        return subscribe(initializeAutoReload(observable, autoReloadAction), onNext, onError);
    }

    protected <T> Disposable subscribeIoAutoReload(Single<T> single,
                                                   final ActionSafe autoReloadAction,
                                                   final ConsumerSafe<T> onNext,
                                                   final ConsumerSafe<Throwable> onError) {
        return subscribe(initializeAutoReload(single.toObservable(), autoReloadAction), onNext, onError);
    }

    protected <T> Disposable subscribeIoAutoReload(Observable<T> observable,
                                                   final ActionSafe autoReloadAction,
                                                   final ConsumerSafe<T> onNext,
                                                   final ActionSafe onComplete,
                                                   final ConsumerSafe<Throwable> onError) {
        return subscribe(initializeAutoReload(observable, autoReloadAction), onNext, onComplete, onError);
    }

    protected <T> Disposable subscribeIoAutoReload(Single<T> single,
                                                   final ActionSafe autoReloadAction,
                                                   final ConsumerSafe<T> onNext,
                                                   final ActionSafe onComplete,
                                                   final ConsumerSafe<Throwable> onError) {
        return subscribe(initializeAutoReload(single.toObservable(), autoReloadAction), onNext, onComplete, onError);
    }
    //endregion

    //region subscribeIoHandleErrorAutoReload

    /**
     * {@see subscribeIoAutoReload} кроме того автоматически обрабатывает ошибки
     */
    protected <T> Disposable subscribeIoHandleErrorAutoReload(Observable<T> observable,
                                                              final ActionSafe autoReloadAction,
                                                              final ConsumerSafe<T> onNext,
                                                              @Nullable final ConsumerSafe<Throwable> onError) {
        return subscribeIoHandleError(initializeAutoReload(observable, autoReloadAction), onNext, onError);
    }

    protected <T> Disposable subscribeIoHandleErrorAutoReload(Single<T> single,
                                                              final ActionSafe autoReloadAction,
                                                              final ConsumerSafe<T> onNext,
                                                              @Nullable final ConsumerSafe<Throwable> onError) {
        return subscribeIoHandleError(initializeAutoReload(single.toObservable(), autoReloadAction), onNext, onError);
    }

    protected <T> Disposable subscribeIoHandleErrorAutoReload(Observable<T> observable,
                                                              final ActionSafe autoReloadAction,
                                                              final ConsumerSafe<T> onNext,
                                                              final ActionSafe onComplete,
                                                              @Nullable final ConsumerSafe<Throwable> onError) {
        return subscribeIoHandleError(initializeAutoReload(observable, autoReloadAction), onNext, onComplete, onError);
    }

    protected <T> Disposable subscribeIoHandleErrorAutoReload(Single<T> single,
                                                              final ActionSafe autoReloadAction,
                                                              final ConsumerSafe<T> onNext,
                                                              final ActionSafe onComplete,
                                                              @Nullable final ConsumerSafe<Throwable> onError) {
        return subscribeIoHandleError(initializeAutoReload(single.toObservable(), autoReloadAction), onNext, onComplete, onError);
    }
    //endregion

    private void handleError(Throwable e, @Nullable ConsumerSafe<Throwable> onError) {
        errorHandler.handleError(e);
        if (onError != null) {
            onError.accept(e);
        }
    }

    private void cancelAutoReload() {
        if (isDisposableActive(autoReloadDisposable)) {
            autoReloadDisposable.dispose();
        }
    }

    @NonNull
    private <T> Observable<T> initializeAutoReload(Observable<T> observable, ActionSafe reloadAction) {
        return observable.doOnError(e -> {
            cancelAutoReload();
            if (connectionProvider.isDisconnected()) {
                autoReloadDisposable = subscribe(connectionProvider.observeConnectionChanges()
                                .filter(connected -> connected)
                                .firstElement()
                                .toObservable(),
                        connected -> reloadAction.run());
            }
        });
    }
}
