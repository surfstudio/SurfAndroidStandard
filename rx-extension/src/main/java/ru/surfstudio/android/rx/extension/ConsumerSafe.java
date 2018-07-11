package ru.surfstudio.android.rx.extension;


import io.reactivex.functions.Consumer;

/**
 * {@link Consumer} без обязательной
 * проверки на {@link Exception}
 *
 * @see Consumer
 */
public interface ConsumerSafe<T> extends Consumer<T> {
    void accept(T t);
}
