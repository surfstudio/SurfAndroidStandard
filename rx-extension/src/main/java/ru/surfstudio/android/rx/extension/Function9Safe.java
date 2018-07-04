package ru.surfstudio.android.rx.extension;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function9;


/**
 * {@link Function9} без обязательной
 * проверки на {@link Exception}
 *
 * @see Function9
 */
public interface Function9Safe<T1, T2, T3, T4, T5, T6, T7, T8, T9, R> extends Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, R> {
    @NonNull
    R apply(@NonNull T1 t1, @NonNull T2 t2, @NonNull T3 t3, @NonNull T4 t4, @NonNull T5 t5, @NonNull T6 t6, @NonNull T7 t7, @NonNull T8 t8, @NonNull T9 t9);
}
