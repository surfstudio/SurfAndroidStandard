package ru.surfstudio.android.core.util.rx;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.exceptions.OnErrorNotImplementedException;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.plugins.RxJavaPlugins;


/**
 * содержит недостающие методы для Observable
 */
public class ObservableUtil {

    public static final SafeAction EMPTY_ACTION = () -> {
    };

    public static final SafeConsumer<Throwable> ON_ERROR_MISSING = throwable ->
            RxJavaPlugins.onError(new OnErrorNotImplementedException(throwable));

    private ObservableUtil() {
        //do nothing
    }

    /**
     * работает также как {@see Observable#combineLatestDelayError}, но еще распараллеливает запросы с помощью {@param scheduler}
     */
    public static <T, R> Observable<R> combineLatestDelayError(Scheduler scheduler,
                                                               List<Observable<? extends T>> sources,
                                                               SafeFunction<? super Object[], ? extends R> combineFunction) {
        sources = toParallel(sources, scheduler);
        return Observable.combineLatestDelayError(sources, combineFunction);
    }

    public static <T1, T2, R> Observable<R> combineLatestDelayError(Scheduler scheduler,
                                                                    Observable<? extends T1> o1,
                                                                    Observable<? extends T2> o2,
                                                                    SafeBiFunction<? super T1, ? super T2, ? extends R> combineFunction) {
        return combineLatestDelayError(scheduler, Arrays.asList(o1, o2), toFunction(combineFunction));
    }

    public static <T1, T2, T3, R> Observable<R> combineLatestDelayError(Scheduler scheduler,
                                                                        Observable<? extends T1> o1,
                                                                        Observable<? extends T2> o2,
                                                                        Observable<? extends T3> o3,
                                                                        SafeFunction3<? super T1, ? super T2, ? super T3, ? extends R> combineFunction) {
        return combineLatestDelayError(scheduler, Arrays.asList(o1, o2, o3), toFunction(combineFunction));
    }

    public static <T1, T2, T3, T4, R> Observable<R> combineLatestDelayError(Scheduler scheduler,
                                                                            Observable<? extends T1> o1,
                                                                            Observable<? extends T2> o2,
                                                                            Observable<? extends T3> o3,
                                                                            Observable<? extends T4> o4,
                                                                            SafeFunction4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> combineFunction) {
        return combineLatestDelayError(scheduler, Arrays.asList(o1, o2, o3, o4), toFunction(combineFunction));
    }

    public static <T1, T2, T3, T4, T5, R> Observable<R> combineLatestDelayError(Scheduler scheduler,
                                                                                Observable<? extends T1> o1,
                                                                                Observable<? extends T2> o2,
                                                                                Observable<? extends T3> o3,
                                                                                Observable<? extends T4> o4,
                                                                                Observable<? extends T5> o5,
                                                                                SafeFunction5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? extends R> combineFunction) {
        return combineLatestDelayError(scheduler, Arrays.asList(o1, o2, o3, o4, o5), toFunction(combineFunction));
    }

    public static <T1, T2, T3, T4, T5, T6, R> Observable<R> combineLatestDelayError(Scheduler scheduler,
                                                                                    Observable<? extends T1> o1,
                                                                                    Observable<? extends T2> o2,
                                                                                    Observable<? extends T3> o3,
                                                                                    Observable<? extends T4> o4,
                                                                                    Observable<? extends T5> o5,
                                                                                    Observable<? extends T6> o6,
                                                                                    SafeFunction6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends R> combineFunction) {
        return combineLatestDelayError(scheduler, Arrays.asList(o1, o2, o3, o4, o5, o6), toFunction(combineFunction));
    }


    public static <T1, T2, T3, T4, T5, T6, T7, R> Observable<R> combineLatestDelayError(Scheduler scheduler,
                                                                                        Observable<? extends T1> o1,
                                                                                        Observable<? extends T2> o2,
                                                                                        Observable<? extends T3> o3,
                                                                                        Observable<? extends T4> o4,
                                                                                        Observable<? extends T5> o5,
                                                                                        Observable<? extends T6> o6,
                                                                                        Observable<? extends T7> o7,
                                                                                        SafeFunction7<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? extends R> combineFunction) {
        return combineLatestDelayError(scheduler, Arrays.asList(o1, o2, o3, o4, o5, o6, o7), toFunction(combineFunction));
    }

    public static <T1, T2, T3, T4, T5, T6, T7, T8, R> Observable<R> combineLatestDelayError(Scheduler scheduler,
                                                                                            Observable<? extends T1> o1,
                                                                                            Observable<? extends T2> o2,
                                                                                            Observable<? extends T3> o3,
                                                                                            Observable<? extends T4> o4,
                                                                                            Observable<? extends T5> o5,
                                                                                            Observable<? extends T6> o6,
                                                                                            Observable<? extends T7> o7,
                                                                                            Observable<? extends T8> o8,
                                                                                            SafeFunction8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? extends R> combineFunction) {
        return combineLatestDelayError(scheduler, Arrays.asList(o1, o2, o3, o4, o5, o6, o7, o8), toFunction(combineFunction));
    }

    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, R> Observable<R> combineLatestDelayError(Scheduler scheduler,
                                                                                                Observable<? extends T1> o1,
                                                                                                Observable<? extends T2> o2,
                                                                                                Observable<? extends T3> o3,
                                                                                                Observable<? extends T4> o4,
                                                                                                Observable<? extends T5> o5,
                                                                                                Observable<? extends T6> o6,
                                                                                                Observable<? extends T7> o7,
                                                                                                Observable<? extends T8> o8,
                                                                                                Observable<? extends T9> o9,
                                                                                                SafeFunction9<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? extends R> combineFunction) {
        return combineLatestDelayError(scheduler, Arrays.asList(o1, o2, o3, o4, o5, o6, o7, o8, o9), toFunction(combineFunction));
    }

    private static <T> List<Observable<? extends T>> toParallel(List<Observable<? extends T>> sources, Scheduler scheduler) {
        return Stream.of(sources)
                .map(source -> source.subscribeOn(scheduler))
                .collect(Collectors.toList());
    }

    public static <T1, T2, R> SafeFunction<Object[], R> toFunction(final SafeBiFunction<? super T1, ? super T2, ? extends R> f) {
        ObjectHelper.requireNonNull(f, "f is null");
        return new Array2Func<>(f);
    }

    public static <T1, T2, T3, R> SafeFunction<Object[], R> toFunction(final SafeFunction3<T1, T2, T3, R> f) {
        ObjectHelper.requireNonNull(f, "f is null");
        return new Array3Func<>(f);
    }

    public static <T1, T2, T3, T4, R> SafeFunction<Object[], R> toFunction(final SafeFunction4<T1, T2, T3, T4, R> f) {
        ObjectHelper.requireNonNull(f, "f is null");
        return new Array4Func<>(f);
    }

    public static <T1, T2, T3, T4, T5, R> SafeFunction<Object[], R> toFunction(final SafeFunction5<T1, T2, T3, T4, T5, R> f) {
        ObjectHelper.requireNonNull(f, "f is null");
        return new Array5Func<>(f);
    }

    public static <T1, T2, T3, T4, T5, T6, R> SafeFunction<Object[], R> toFunction(
            final SafeFunction6<T1, T2, T3, T4, T5, T6, R> f) {
        ObjectHelper.requireNonNull(f, "f is null");
        return new Array6Func<>(f);
    }

    public static <T1, T2, T3, T4, T5, T6, T7, R> SafeFunction<Object[], R> toFunction(
            final SafeFunction7<T1, T2, T3, T4, T5, T6, T7, R> f) {
        ObjectHelper.requireNonNull(f, "f is null");
        return new Array7Func<>(f);
    }

    public static <T1, T2, T3, T4, T5, T6, T7, T8, R> SafeFunction<Object[], R> toFunction(
            final SafeFunction8<T1, T2, T3, T4, T5, T6, T7, T8, R> f) {
        ObjectHelper.requireNonNull(f, "f is null");
        return new Array8Func<>(f);
    }

    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9, R> SafeFunction<Object[], R> toFunction(
            final SafeFunction9<T1, T2, T3, T4, T5, T6, T7, T8, T9, R> f) {
        ObjectHelper.requireNonNull(f, "f is null");
        return new Array9Func<>(f);
    }

    private static final class Array2Func<T1, T2, R> implements SafeFunction<Object[], R> {
        final SafeBiFunction<? super T1, ? super T2, ? extends R> f;

        Array2Func(SafeBiFunction<? super T1, ? super T2, ? extends R> f) {
            this.f = f;
        }

        @SuppressWarnings("unchecked")
        @Override
        public R apply(Object[] a) {
            if (a.length != 2) {
                throw new IllegalArgumentException("Array of size 2 expected but got " + a.length);
            }
            return f.apply((T1) a[0], (T2) a[1]);
        }
    }

    private static final class Array3Func<T1, T2, T3, R> implements SafeFunction<Object[], R> {
        final SafeFunction3<T1, T2, T3, R> f;

        Array3Func(SafeFunction3<T1, T2, T3, R> f) {
            this.f = f;
        }

        @SuppressWarnings("unchecked")
        @Override
        public R apply(Object[] a) {
            if (a.length != 3) {
                throw new IllegalArgumentException("Array of size 3 expected but got " + a.length);
            }
            return f.apply((T1) a[0], (T2) a[1], (T3) a[2]);
        }
    }

    private static final class Array4Func<T1, T2, T3, T4, R> implements SafeFunction<Object[], R> {
        final SafeFunction4<T1, T2, T3, T4, R> f;

        Array4Func(SafeFunction4<T1, T2, T3, T4, R> f) {
            this.f = f;
        }

        @SuppressWarnings("unchecked")
        @Override
        public R apply(Object[] a) {
            if (a.length != 4) {
                throw new IllegalArgumentException("Array of size 4 expected but got " + a.length);
            }
            return f.apply((T1) a[0], (T2) a[1], (T3) a[2], (T4) a[3]);
        }
    }

    private static final class Array5Func<T1, T2, T3, T4, T5, R> implements SafeFunction<Object[], R> {
        private final SafeFunction5<T1, T2, T3, T4, T5, R> f;

        Array5Func(SafeFunction5<T1, T2, T3, T4, T5, R> f) {
            this.f = f;
        }

        @SuppressWarnings("unchecked")
        @Override
        public R apply(Object[] a) {
            if (a.length != 5) {
                throw new IllegalArgumentException("Array of size 5 expected but got " + a.length);
            }
            return f.apply((T1) a[0], (T2) a[1], (T3) a[2], (T4) a[3], (T5) a[4]);
        }
    }

    private static final class Array6Func<T1, T2, T3, T4, T5, T6, R> implements SafeFunction<Object[], R> {
        final SafeFunction6<T1, T2, T3, T4, T5, T6, R> f;

        Array6Func(SafeFunction6<T1, T2, T3, T4, T5, T6, R> f) {
            this.f = f;
        }

        @SuppressWarnings("unchecked")
        @Override
        public R apply(Object[] a) {
            if (a.length != 6) {
                throw new IllegalArgumentException("Array of size 6 expected but got " + a.length);
            }
            return f.apply((T1) a[0], (T2) a[1], (T3) a[2], (T4) a[3], (T5) a[4], (T6) a[5]);
        }
    }

    private static final class Array7Func<T1, T2, T3, T4, T5, T6, T7, R> implements SafeFunction<Object[], R> {
        final SafeFunction7<T1, T2, T3, T4, T5, T6, T7, R> f;

        Array7Func(SafeFunction7<T1, T2, T3, T4, T5, T6, T7, R> f) {
            this.f = f;
        }

        @SuppressWarnings("unchecked")
        @Override
        public R apply(Object[] a) {
            if (a.length != 7) {
                throw new IllegalArgumentException("Array of size 7 expected but got " + a.length);
            }
            return f.apply((T1) a[0], (T2) a[1], (T3) a[2], (T4) a[3], (T5) a[4], (T6) a[5], (T7) a[6]);
        }
    }

    private static final class Array8Func<T1, T2, T3, T4, T5, T6, T7, T8, R> implements SafeFunction<Object[], R> {
        final SafeFunction8<T1, T2, T3, T4, T5, T6, T7, T8, R> f;

        Array8Func(SafeFunction8<T1, T2, T3, T4, T5, T6, T7, T8, R> f) {
            this.f = f;
        }

        @SuppressWarnings("unchecked")
        @Override
        public R apply(Object[] a) {
            if (a.length != 8) {
                throw new IllegalArgumentException("Array of size 8 expected but got " + a.length);
            }
            return f.apply((T1) a[0], (T2) a[1], (T3) a[2], (T4) a[3], (T5) a[4], (T6) a[5], (T7) a[6], (T8) a[7]);
        }
    }

    private static final class Array9Func<T1, T2, T3, T4, T5, T6, T7, T8, T9, R> implements SafeFunction<Object[], R> {
        final SafeFunction9<T1, T2, T3, T4, T5, T6, T7, T8, T9, R> f;

        Array9Func(SafeFunction9<T1, T2, T3, T4, T5, T6, T7, T8, T9, R> f) {
            this.f = f;
        }

        @SuppressWarnings("unchecked")
        @Override
        public R apply(Object[] a) {
            if (a.length != 9) {
                throw new IllegalArgumentException("Array of size 9 expected but got " + a.length);
            }
            return f.apply((T1) a[0], (T2) a[1], (T3) a[2], (T4) a[3], (T5) a[4], (T6) a[5], (T7) a[6], (T8) a[7], (T9) a[8]);
        }
    }
}
