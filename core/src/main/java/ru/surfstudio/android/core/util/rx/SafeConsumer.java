package ru.surfstudio.android.core.util.rx;


import io.reactivex.functions.Consumer;

/**
 * {@link Consumer} без обязательной
 * проверки на {@link Exception}
 *
 * @see Consumer
 */
public interface SafeConsumer<T> extends Consumer<T> {
    void accept(T t);
}
