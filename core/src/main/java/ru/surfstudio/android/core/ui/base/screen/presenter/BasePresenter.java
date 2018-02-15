package ru.surfstudio.android.core.ui.base.screen.presenter;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.agna.ferro.rx.ObservableOperatorFreeze;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.observers.LambdaObserver;
import ru.surfstudio.android.connection.ConnectionProvider;
import ru.surfstudio.android.core.app.scheduler.SchedulersProvider;
import ru.surfstudio.android.core.ui.base.navigation.activity.navigator.ActivityNavigator;
import ru.surfstudio.android.core.ui.base.screen.view.HandleableErrorView;
import ru.surfstudio.android.core.ui.base.screen.view.core.CoreView;
import ru.surfstudio.android.rx.extension.ActionSafe;
import ru.surfstudio.android.rx.extension.ConsumerSafe;


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
public abstract class BasePresenter<V extends CoreView & HandleableErrorView> extends CorePresenter<V> {

    private final ActivityNavigator activityNavigator;
    private final SchedulersProvider schedulersProvider;
    private final ConnectionProvider connectionProvider;
    private Disposable autoReloadDisposable;

    public BasePresenter(BasePresenterDependency basePresenterDependency) {
        super(basePresenterDependency.getEventDelegateManager(), basePresenterDependency.getScreenState());
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
     * Работает также как {@link #subscribe}, кроме того автоматически обрабатывает ошибки,
     * см {@link HandleableErrorView} и переводит выполенения потока в фон
     */
    protected <T> Disposable subscribeIoHandleError(Observable<T> observable,
                                                    final ConsumerSafe<T> onNext) {
        return subscribeIoHandleError(observable, onNext, null);
    }

    /**
     * Работает также как {@link #subscribe}, кроме того автоматически обрабатывает ошибки,
     * см {@link HandleableErrorView} и переводит выполенения потока в фон
     */
    protected <T> Disposable subscribeIoHandleError(Observable<T> observable,
                                                    final ConsumerSafe<T> onNext,
                                                    final ConsumerSafe<Throwable> onError) {
        observable = observable.subscribeOn(schedulersProvider.worker());
        return subscribe(observable, onNext, e -> handleError(e, onError));
    }

    /**
     * Работает также как {@link #subscribe}, кроме того автоматически обрабатывает ошибки,
     * см {@link HandleableErrorView} и переводит выполенения потока в фон
     */
    protected <T> Disposable subscribeIoHandleError(Observable<T> observable,
                                                    final ConsumerSafe<T> onNext,
                                                    final ActionSafe onComplete,
                                                    final ConsumerSafe<Throwable> onError) {
        observable = observable.subscribeOn(schedulersProvider.worker());
        return subscribe(observable, onNext, onComplete, e -> handleError(e, onError));
    }


    protected <T> Disposable subscribeIo(Observable<T> observable,
                                         final ConsumerSafe<T> onNext,
                                         final ConsumerSafe<Throwable> onError) {
        observable = observable.subscribeOn(schedulersProvider.worker());
        return subscribe(observable, onNext, onError);
    }

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

    /**
     * {@see subscribeIoAutoReload} кроме того автоматически обрабатывает ошибки
     */
    protected <T> Disposable subscribeIoHandleErrorAutoReload(Observable<T> observable,
                                                              final ActionSafe autoReloadAction,
                                                              final ConsumerSafe<T> onNext,
                                                              @Nullable final ConsumerSafe<Throwable> onError) {
        return subscribeIoHandleError(initializeAutoReload(observable, autoReloadAction), onNext, onError);
    }

    private void handleError(Throwable e, @Nullable ConsumerSafe<Throwable> onError) {
        getView().handleError(e);
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
