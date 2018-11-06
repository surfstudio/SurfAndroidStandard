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
import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency

/**
 * Created by vsokolova on 15.03.18.
 */

class MockPresenter(basePresenterDependency: BasePresenterDependency) : BasePresenter<MockActivityView>(basePresenterDependency) {


    fun <T> testSubscribe(observable: Observable<T>, operator: ObservableOperatorFreeze<T>, observer: LambdaObserver<T>): Disposable {
        return observable.subscribeUi(operator, observer)
    }

    fun <T> testSubscribe(single: Single<T>, operator: SingleOperatorFreeze<T>, observer: DisposableSingleObserver<T>): Disposable {
        return single.subscribeUi(operator, observer)
    }

    fun testSubscribe(completable: Completable, operator: CompletableOperatorFreeze, observer: DisposableCompletableObserver): Disposable {
        return completable.subscribeUi(operator, observer);
    }

    fun <T> testSubscribe(maybe: Maybe<T>, operator: MaybeOperatorFreeze<T>, observer: DisposableMaybeObserver<T>): Disposable {
        return maybe.subscribeUi(operator, observer)
    }

    fun <T> testSubscribeWithoutFreezing(observable: Observable<T>, observer: LambdaObserver<T>): Disposable {
        return observable.subscribeWithoutFreezingUi(observer)
    }

    fun <T> testSubscribeWithoutFreezing(single: Single<T>, subscriber: DisposableSingleObserver<T>): Disposable {
        return single.subscribeWithoutFreezingUi(subscriber)
    }


    fun testSubscribeWithoutFreezing(completable: Completable, subscriber: DisposableCompletableObserver): Disposable {
        return completable.subscribeWithoutFreezingUi(subscriber)
    }

    fun <T> testSubscribeWithoutFreezing(maybe: Maybe<T>, subscriber: DisposableMaybeObserver<T>): Disposable {
        return maybe.subscribeWithoutFreezingUi(subscriber)
    }

    fun <T> testSubscribeIoHandleError(observable: Observable<T>, onNext: (T) -> Unit): Disposable {
        return observable.subscribeByIoHandleError(onNext)
    }

    fun <T> testSubscribeIoHandleError(single: Single<T>, onSuccess: (T) -> Unit): Disposable {
        return single.subscribeByIoHandleError(onSuccess)
    }


    fun testSubscribeIoHandleError(completable: Completable, onComplete: () -> Unit): Disposable {
        return completable.subscribeByIoHandleError(onComplete)
    }


    fun <T> testSubscribeIoHandleError(maybe: Maybe<T>, onSuccess: (T) -> Unit, onComplete: () -> Unit): Disposable {
        return maybe.subscribeByIoHandleError(onSuccess, onComplete)
    }


    fun <T> testSubscribeIoHandleError(observable: Observable<T>, onNext: (T) -> Unit, onError: (Throwable) -> Unit): Disposable {
        return observable.subscribeByIoHandleError(onNext, onError)
    }


    fun <T> testSubscribeIoHandleError(observable: Observable<T>, onNext: (T) -> Unit, onComplete: () -> Unit, onError: (Throwable) -> Unit): Disposable {
        return observable.subscribeByIoHandleError(onNext, onComplete, onError)
    }


    fun <T> testSubscribeIoHandleError(single: Single<T>, onSuccess: (T) -> Unit, onError: (Throwable) -> Unit): Disposable {
        return single.subscribeByIoHandleError(onSuccess, onError);
    }


    fun testSubscribeIoHandleError(completable: Completable, onComplete: () -> Unit, onError: (Throwable) -> Unit): Disposable {
        return completable.subscribeByIoHandleError(onComplete, onError)
    }


    fun <T> testSubscribeIoHandleError(maybe: Maybe<T>, onSuccess: (T) -> Unit, onComplete: () -> Unit, onError: (Throwable) -> Unit): Disposable {
        return maybe.subscribeByIoHandleError(onSuccess, onComplete, onError)
    }


    fun <T> testSubscribeIo(observable: Observable<T>, onNext: (T) -> Unit, onError: (Throwable) -> Unit): Disposable {
        return observable.subscribeByIo(onNext, onError)
    }


    fun <T> testSubscribeIo(single: Single<T>, onSuccess: (T) -> Unit, onError: (Throwable) -> Unit): Disposable {
        return single.subscribeByIo(onSuccess, onError)
    }


    fun testSubscribeIo(completable: Completable, onComplete: () -> Unit, onError: (Throwable) -> Unit): Disposable {
        return completable.subscribeByIo(onComplete, onError)
    }


    fun <T> testSubscribeIo(observable: Observable<T>, onNext: (T) -> Unit, onComplete: () -> Unit, onError: (Throwable) -> Unit): Disposable {
        return observable.subscribeByIo(onNext, onComplete, onError)
    }


    fun <T> testSubscribeIo(maybe: Maybe<T>, onNext: (T) -> Unit, onSuccess: () -> Unit, onError: (Throwable) -> Unit): Disposable {
        return maybe.subscribeByIo(onNext, onSuccess, onError)
    }


    fun <T> testSubscribeIoAutoReload(observable: Observable<T>, autoReloadAction: () -> Unit, onNext: (T) -> Unit, onError: (Throwable) -> Unit): Disposable {
        return observable.subscribeByIoAutoReload(autoReloadAction, onNext, onError)
    }


    fun <T> testSubscribeIoAutoReload(observable: Observable<T>, autoReloadAction: () -> Unit, onNext: (T) -> Unit, onComplete: () -> Unit, onError: (Throwable) -> Unit): Disposable {
        return observable.subscribeByIoAutoReload(autoReloadAction, onNext, onComplete, onError)
    }


    fun <T> testSubscribeIoAutoReload(single: Single<T>, autoReloadAction: () -> Unit, onSuccess: (T) -> Unit, onError: (Throwable) -> Unit): Disposable {
        return single.subscribeByIoAutoReload(autoReloadAction, onSuccess, onError)
    }


    fun testSubscribeIoAutoReload(completable: Completable, autoReloadAction: () -> Unit, onComplete: () -> Unit, onError: (Throwable) -> Unit): Disposable {
        return completable.subscribeByIoAutoReload(autoReloadAction, onComplete, onError)
    }


    fun <T> testSubscribeIoAutoReload(maybe: Maybe<T>, autoReloadAction: () -> Unit, onSuccess: (T) -> Unit, onComplete: () -> Unit, onError: (Throwable) -> Unit): Disposable {
        return maybe.subscribeByIoAutoReload(autoReloadAction, onSuccess, onComplete, onError)
    }


    fun <T> testSubscribeIoHandleErrorAutoReload(observable: Observable<T>, autoReloadAction: () -> Unit, onNext: (T) -> Unit, onError: (Throwable) -> Unit): Disposable {
        return observable.subscribeByIoHandleErrorAutoReload(autoReloadAction, onNext, onError)
    }

    fun <T> testSubscribeIoHandleErrorAutoReload(observable: Observable<T>, autoReloadAction: () -> Unit, onNext: (T) -> Unit, onComplete: () -> Unit, onError: (Throwable) -> Unit): Disposable {
        return observable.subscribeByIoHandleErrorAutoReload(autoReloadAction, onNext, onComplete, onError)
    }


    fun <T> testSubscribeIoHandleErrorAutoReload(single: Single<T>, autoReloadAction: () -> Unit, onSuccess: (T) -> Unit, onError: (Throwable) -> Unit): Disposable {
        return single.subscribeByIoHandleErrorAutoReload(autoReloadAction, onSuccess, onError)
    }


    fun testSubscribeIoHandleErrorAutoReload(completable: Completable, autoReloadAction: () -> Unit, onComplete: () -> Unit, onError: (Throwable) -> Unit): Disposable {
        return completable.subscribeByIoHandleErrorAutoReload(autoReloadAction, onComplete, onError)
    }


    fun <T> testSubscribeIoHandleErrorAutoReload(maybe: Maybe<T>, autoReloadAction: () -> Unit, onSuccess: (T) -> Unit, onComplete: () -> Unit, onError: (Throwable) -> Unit): Disposable {
        return maybe.subscribeByIoHandleErrorAutoReload(autoReloadAction, onSuccess, onComplete, onError)
    }
}
