package ru.surfstudio.android.rx.extension;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function4;


/**
 * {@link Function4} без обязательной
 * проверки на {@link Exception}
 *
 * @see Function4
 */
public interface Function4Safe<T1, T2, T3, T4, R> extends Function4<T1, T2, T3, T4, R> {
    @NonNull
    R apply(@NonNull T1 t1, @NonNull T2 t2, @NonNull T3 t3, @NonNull T4 t4);
}
