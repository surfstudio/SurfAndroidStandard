
import com.agna.ferro.rx.CompletableOperatorFreeze;
import com.agna.ferro.rx.MaybeOperatorFreeze;
import com.agna.ferro.rx.ObservableOperatorFreeze;
import com.agna.ferro.rx.SingleOperatorFreeze;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.observers.LambdaObserver;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableMaybeObserver;
import io.reactivex.observers.DisposableSingleObserver;
import ru.surfstudio.android.core.mvp.presenter.BasePresenter;
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency;
import ru.surfstudio.android.rx.extension.ActionSafe;
import ru.surfstudio.android.rx.extension.ConsumerSafe;

/**
 * Created by vsokolova on 15.03.18.
 */

public class MockPresenter extends BasePresenter<MockActivityView> {

    public MockPresenter(BasePresenterDependency basePresenterDependency) {
        super(basePresenterDependency);
    }


    public <T> Disposable subscribe(Observable<T> observable, ObservableOperatorFreeze<T> operator, LambdaObserver<T> observer) {
        return super.subscribe(observable, operator, observer);
    }


    public <T> Disposable subscribe(Single<T> single, SingleOperatorFreeze<T> operator, DisposableSingleObserver<T> observer) {
        return super.subscribe(single, operator, observer);
    }


    public Disposable subscribe(Completable completable, CompletableOperatorFreeze operator, DisposableCompletableObserver observer) {
        return super.subscribe(completable, operator, observer);
    }


    public <T> Disposable subscribe(Maybe<T> maybe, MaybeOperatorFreeze<T> operator, DisposableMaybeObserver<T> observer) {
        return super.subscribe(maybe, operator, observer);
    }


    public <T> Disposable subscribeWithoutFreezing(Observable<T> observable, LambdaObserver<T> observer) {
        return super.subscribeWithoutFreezing(observable, observer);
    }


    public <T> Disposable subscribeWithoutFreezing(Single<T> single, DisposableSingleObserver<T> subscriber) {
        return super.subscribeWithoutFreezing(single, subscriber);
    }


    public Disposable subscribeWithoutFreezing(Completable completable, DisposableCompletableObserver subscriber) {
        return super.subscribeWithoutFreezing(completable, subscriber);
    }


    public <T> Disposable subscribeWithoutFreezing(Maybe<T> maybe, DisposableMaybeObserver<T> subscriber) {
        return super.subscribeWithoutFreezing(maybe, subscriber);
    }


    public <T> Disposable subscribeIoHandleError(Observable<T> observable, ConsumerSafe<T> onNext) {
        return super.subscribeIoHandleError(observable, onNext);
    }


    public <T> Disposable subscribeIoHandleError(Single<T> single, ConsumerSafe<T> onSuccess) {
        return super.subscribeIoHandleError(single, onSuccess);
    }


    public Disposable subscribeIoHandleError(Completable completable, ActionSafe onComplete) {
        return super.subscribeIoHandleError(completable, onComplete);
    }


    public <T> Disposable subscribeIoHandleError(Maybe<T> maybe, ConsumerSafe<T> onSuccess, ActionSafe onComplete) {
        return super.subscribeIoHandleError(maybe, onSuccess, onComplete);
    }


    public <T> Disposable subscribeIoHandleError(Observable<T> observable, ConsumerSafe<T> onNext, ConsumerSafe<Throwable> onError) {
        return super.subscribeIoHandleError(observable, onNext, onError);
    }


    public <T> Disposable subscribeIoHandleError(Observable<T> observable, ConsumerSafe<T> onNext, ActionSafe onComplete, ConsumerSafe<Throwable> onError) {
        return super.subscribeIoHandleError(observable, onNext, onComplete, onError);
    }


    public <T> Disposable subscribeIoHandleError(Single<T> single, ConsumerSafe<T> onSuccess, ConsumerSafe<Throwable> onError) {
        return super.subscribeIoHandleError(single, onSuccess, onError);
    }


    public Disposable subscribeIoHandleError(Completable completable, ActionSafe onComplete, ConsumerSafe<Throwable> onError) {
        return super.subscribeIoHandleError(completable, onComplete, onError);
    }


    public <T> Disposable subscribeIoHandleError(Maybe<T> maybe, ConsumerSafe<T> onSuccess, ActionSafe onComplete, ConsumerSafe<Throwable> onError) {
        return super.subscribeIoHandleError(maybe, onSuccess, onComplete, onError);
    }


    public <T> Disposable subscribeIo(Observable<T> observable, ConsumerSafe<T> onNext, ConsumerSafe<Throwable> onError) {
        return super.subscribeIo(observable, onNext, onError);
    }


    public <T> Disposable subscribeIo(Single<T> single, ConsumerSafe<T> onSuccess, ConsumerSafe<Throwable> onError) {
        return super.subscribeIo(single, onSuccess, onError);
    }


    public Disposable subscribeIo(Completable completable, ActionSafe onComplete, ConsumerSafe<Throwable> onError) {
        return super.subscribeIo(completable, onComplete, onError);
    }


    public <T> Disposable subscribeIo(Observable<T> observable, ConsumerSafe<T> onNext, ActionSafe onComplete, ConsumerSafe<Throwable> onError) {
        return super.subscribeIo(observable, onNext, onComplete, onError);
    }


    public <T> Disposable subscribeIo(Maybe<T> maybe, ConsumerSafe<T> onNext, ActionSafe onSuccess, ConsumerSafe<Throwable> onError) {
        return super.subscribeIo(maybe, onNext, onSuccess, onError);
    }


    public <T> Disposable subscribeIoAutoReload(Observable<T> observable, ActionSafe autoReloadAction, ConsumerSafe<T> onNext, ConsumerSafe<Throwable> onError) {
        return super.subscribeIoAutoReload(observable, autoReloadAction, onNext, onError);
    }


    public <T> Disposable subscribeIoAutoReload(Observable<T> observable, ActionSafe autoReloadAction, ConsumerSafe<T> onNext, ActionSafe onComplete, ConsumerSafe<Throwable> onError) {
        return super.subscribeIoAutoReload(observable, autoReloadAction, onNext, onComplete, onError);
    }


    public <T> Disposable subscribeIoAutoReload(Single<T> single, ActionSafe autoReloadAction, ConsumerSafe<T> onSuccess, ConsumerSafe<Throwable> onError) {
        return super.subscribeIoAutoReload(single, autoReloadAction, onSuccess, onError);
    }


    public Disposable subscribeIoAutoReload(Completable completable, ActionSafe autoReloadAction, ActionSafe onComplete, ConsumerSafe<Throwable> onError) {
        return super.subscribeIoAutoReload(completable, autoReloadAction, onComplete, onError);
    }


    public <T> Disposable subscribeIoAutoReload(Maybe<T> maybe, ActionSafe autoReloadAction, ConsumerSafe<T> onSuccess, ActionSafe onComplete, ConsumerSafe<Throwable> onError) {
        return super.subscribeIoAutoReload(maybe, autoReloadAction, onSuccess, onComplete, onError);
    }


    public <T> Disposable subscribeIoHandleErrorAutoReload(Observable<T> observable, ActionSafe autoReloadAction, ConsumerSafe<T> onNext, ConsumerSafe<Throwable> onError) {
        return super.subscribeIoHandleErrorAutoReload(observable, autoReloadAction, onNext, onError);
    }

    public <T> Disposable subscribeIoHandleErrorAutoReload(Observable<T> observable, ActionSafe autoReloadAction, ConsumerSafe<T> onNext, ActionSafe onComplete, ConsumerSafe<Throwable> onError) {
        return super.subscribeIoHandleErrorAutoReload(observable, autoReloadAction, onNext, onComplete, onError);
    }


    public <T> Disposable subscribeIoHandleErrorAutoReload(Single<T> single, ActionSafe autoReloadAction, ConsumerSafe<T> onSuccess, ConsumerSafe<Throwable> onError) {
        return super.subscribeIoHandleErrorAutoReload(single, autoReloadAction, onSuccess, onError);
    }


    public Disposable subscribeIoHandleErrorAutoReload(Completable completable, ActionSafe autoReloadAction, ActionSafe onComplete, ConsumerSafe<Throwable> onError) {
        return super.subscribeIoHandleErrorAutoReload(completable, autoReloadAction, onComplete, onError);
    }


    public <T> Disposable subscribeIoHandleErrorAutoReload(Maybe<T> maybe, ActionSafe autoReloadAction, ConsumerSafe<T> onSuccess, ActionSafe onComplete, ConsumerSafe<Throwable> onError) {
        return super.subscribeIoHandleErrorAutoReload(maybe, autoReloadAction, onSuccess, onComplete, onError);
    }
}
