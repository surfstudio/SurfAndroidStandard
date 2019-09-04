package ru.surfstudio.android.rx.extension;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function5;


/**
 * {@link Function5} без обязательной
 * проверки на {@link Exception}
 *
 * @see Function5
 */
public interface Function5Safe<T1, T2, T3, T4, T5, R> extends Function5<T1, T2, T3, T4, T5, R> {
    @NonNull
    R apply(@NonNull T1 t1, @NonNull T2 t2, @NonNull T3 t3, @NonNull T4 t4, @NonNull T5 t5);
}
