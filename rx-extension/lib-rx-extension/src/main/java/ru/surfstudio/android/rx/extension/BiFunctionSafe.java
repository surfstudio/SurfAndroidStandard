package ru.surfstudio.android.rx.extension;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;

/**
 * {@link BiFunction} без обязательной
 * проверки на {@link Exception}
 *
 * @see BiFunction
 */
public interface BiFunctionSafe<T1, T2, R> extends BiFunction<T1, T2, R> {
    @Override
    @NonNull
    R apply(@NonNull T1 t1, @NonNull T2 t2);
}
