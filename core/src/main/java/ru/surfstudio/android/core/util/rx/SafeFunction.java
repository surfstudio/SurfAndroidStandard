package ru.surfstudio.android.core.util.rx;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;


/**
 * {@link Function} без обязательной
 * проверки на {@link Exception}
 *
 * @see Function
 */
public interface SafeFunction<T, R> extends Function<T, R> {
    @Override
    R apply(@NonNull T t);
}
