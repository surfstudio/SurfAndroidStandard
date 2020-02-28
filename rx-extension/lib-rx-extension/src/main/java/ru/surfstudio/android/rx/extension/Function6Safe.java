package ru.surfstudio.android.rx.extension;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function6;


/**
 * {@link Function6} без обязательной
 * проверки на {@link Exception}
 *
 * @see Function6
 */
public interface Function6Safe<T1, T2, T3, T4, T5, T6, R> extends Function6<T1, T2, T3, T4, T5, T6, R> {
    @NonNull
    R apply(@NonNull T1 t1, @NonNull T2 t2, @NonNull T3 t3, @NonNull T4 t4, @NonNull T5 t5, @NonNull T6 t6);
}
