import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import ru.surfstudio.android.core.mvp.presenter.BasePresenter
import ru.surfstudio.android.core.mvp.presenter.BasePresenterDependency

/**
 * Created by vsokolova on 15.03.18.
 */

class MockPresenter(basePresenterDependency: BasePresenterDependency) : BasePresenter<MockActivityView>(basePresenterDependency) {

    fun <T> testSubscribeIoHandleError(observable: Observable<T>, onNext: (T) -> Unit): Disposable {
        return observable.subscribeIoHandleErrorBy(onNext)
    }

    fun <T> testSubscribeIoHandleError(single: Single<T>, onSuccess: (T) -> Unit): Disposable {
        return single.subscribeIoHandleErrorBy(onSuccess)
    }


    fun testSubscribeIoHandleError(completable: Completable, onComplete: () -> Unit): Disposable {
        return completable.subscribeIoHandleErrorBy(onComplete)
    }


    fun <T> testSubscribeIoHandleError(maybe: Maybe<T>, onSuccess: (T) -> Unit, onComplete: () -> Unit): Disposable {
        return maybe.subscribeIoHandleErrorBy(onSuccess, onComplete)
    }


    fun <T> testSubscribeIoHandleError(observable: Observable<T>, onNext: (T) -> Unit, onError: (Throwable) -> Unit): Disposable {
        return observable.subscribeIoHandleErrorBy(onNext, onError)
    }


    fun <T> testSubscribeIoHandleError(observable: Observable<T>, onNext: (T) -> Unit, onComplete: () -> Unit, onError: (Throwable) -> Unit): Disposable {
        return observable.subscribeIoHandleErrorBy(onNext, onComplete, onError)
    }


    fun <T> testSubscribeIoHandleError(single: Single<T>, onSuccess: (T) -> Unit, onError: (Throwable) -> Unit): Disposable {
        return single.subscribeIoHandleErrorBy(onSuccess, onError);
    }


    fun testSubscribeIoHandleError(completable: Completable, onComplete: () -> Unit, onError: (Throwable) -> Unit): Disposable {
        return completable.subscribeIoHandleErrorBy(onComplete, onError)
    }


    fun <T> testSubscribeIoHandleError(maybe: Maybe<T>, onSuccess: (T) -> Unit, onComplete: () -> Unit, onError: (Throwable) -> Unit): Disposable {
        return maybe.subscribeIoHandleErrorBy(onSuccess, onComplete, onError)
    }


    fun <T> testSubscribeIo(observable: Observable<T>, onNext: (T) -> Unit, onError: (Throwable) -> Unit): Disposable {
        return observable.subscribeIoBy(onNext, onError)
    }


    fun <T> testSubscribeIo(single: Single<T>, onSuccess: (T) -> Unit, onError: (Throwable) -> Unit): Disposable {
        return single.subscribeIoBy(onSuccess, onError)
    }


    fun testSubscribeIo(completable: Completable, onComplete: () -> Unit, onError: (Throwable) -> Unit): Disposable {
        return completable.subscribeIoBy(onComplete, onError)
    }


    fun <T> testSubscribeIo(observable: Observable<T>, onNext: (T) -> Unit, onComplete: () -> Unit, onError: (Throwable) -> Unit): Disposable {
        return observable.subscribeIoBy(onNext, onComplete, onError)
    }


    fun <T> testSubscribeIo(maybe: Maybe<T>, onNext: (T) -> Unit, onSuccess: () -> Unit, onError: (Throwable) -> Unit): Disposable {
        return maybe.subscribeIoBy(onNext, onSuccess, onError)
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
