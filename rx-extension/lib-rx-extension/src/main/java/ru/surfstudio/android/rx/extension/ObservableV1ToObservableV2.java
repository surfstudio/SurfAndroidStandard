package ru.surfstudio.android.rx.extension;

/**
 * Convert a V1 Observable into a V2 Observable, composing cancellation.
 *
 * Добавлен для поддержки старого слоя интерактор
 *
 * @param <T> the value type
 */
public class ObservableV1ToObservableV2<T> extends io.reactivex.Observable<T> {

    final rx.Observable<T> source;

    public ObservableV1ToObservableV2(rx.Observable<T> source) {
        this.source = source;
    }

    @Override
    protected void subscribeActual(io.reactivex.Observer<? super T> s) {
        ObservableSubscriber<T> parent = new ObservableSubscriber<T>(s);
        s.onSubscribe(parent);

        source.unsafeSubscribe(parent);
    }

    static final class ObservableSubscriber<T> extends rx.Subscriber<T> implements io.reactivex.disposables.Disposable {

        final io.reactivex.Observer<? super T> actual;

        boolean done;

        ObservableSubscriber(io.reactivex.Observer<? super T> actual) {
            this.actual = actual;
        }

        @Override
        public void onNext(T t) {
            if (done) {
                return;
            }
            if (t == null) {
                unsubscribe();
                onError(new NullPointerException(
                    "The upstream 1.x Observable signalled a null value which is not supported in 2.x"));
            } else {
                actual.onNext(t);
            }
        }

        @Override
        public void onError(Throwable e) {
            if (done) {
                io.reactivex.plugins.RxJavaPlugins.onError(e);
                return;
            }
            done = true;
            actual.onError(e);
            unsubscribe(); // v1 expects an unsubscribe call when terminated
        }

        @Override
        public void onCompleted() {
            if (done) {
                return;
            }
            done = true;
            actual.onComplete();
            unsubscribe(); // v1 expects an unsubscribe call when terminated
        }

        @Override
        public void dispose() {
            unsubscribe();
        }

        @Override
        public boolean isDisposed() {
            return isUnsubscribed();
        }
    }
}
