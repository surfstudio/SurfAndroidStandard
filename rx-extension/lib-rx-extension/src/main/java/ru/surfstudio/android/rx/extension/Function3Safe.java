package ru.surfstudio.android.rx.extension;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function3;


/**
 * {@link Function3} без обязательной
 * проверки на {@link Exception}
 *
 * @see Function3
 */
public interface Function3Safe<T1, T2, T3, R> extends Function3<T1, T2, T3, R> {
    @NonNull
    R apply(@NonNull T1 t1, @NonNull T2 t2, @NonNull T3 t3);
}
